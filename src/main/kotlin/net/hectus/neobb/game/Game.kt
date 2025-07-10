package net.hectus.neobb.game

import com.marcpg.libpg.data.modifiable.ModifiableImpl
import com.marcpg.libpg.data.time.Time
import com.marcpg.libpg.display.*
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.*
import net.hectus.neobb.NeoBB
import net.hectus.neobb.external.cosmetic.EffectManager
import net.hectus.neobb.external.rating.Rating
import net.hectus.neobb.game.util.*
import net.hectus.neobb.matrix.Arena
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.StructureTurn
import net.hectus.neobb.modes.turn.default_game.TDefaultWarp
import net.hectus.neobb.modes.turn.default_game.TTimeLimit
import net.hectus.neobb.modes.turn.default_game.WarpTurn
import net.hectus.neobb.modes.turn.person_game.WinConCategory
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.*
import net.kyori.adventure.title.Title
import org.bukkit.*
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import kotlin.math.cos
import kotlin.math.sin
import kotlin.reflect.KClass

abstract class Game(val world: World, private val bukkitPlayers: List<Player>, val difficulty: GameDifficulty = Constants.DEFAULT_DIFFICULTY): ModifiableImpl(), Ticking {
    val id: String = Randomizer.generateRandomString(Constants.GAME_ID_LENGTH, Constants.GAME_ID_CHARSET)
    lateinit var arena: Arena
    val effectManager = EffectManager()
    val turnScheduler = TurnScheduler()

    lateinit var initialPlayers: List<NeoPlayer> private set
    private lateinit var initialTarget: ForwardingMinecraftReceiver
    lateinit var players: MutableList<NeoPlayer> private set
    val history: MutableList<Turn<*>> = mutableListOf()

    val allowed: MutableSet<KClass<*>> = mutableSetOf()

    abstract val info: GameInfo

    open val scoreboard: ((NeoPlayer) -> SimpleScoreboard)? = null
    open val actionBar: ((NeoPlayer) -> SimpleActionBar)? = null

    // ==================================================
    // ============ PUBLIC GETTER VARIABLES =============
    // ==================================================

    var warp: WarpTurn = TDefaultWarp(null, Configuration.SPAWN_CORD, null)
    var playedWarps: List<WarpTurn> = mutableListOf(warp)

    var time: MinecraftTime = MinecraftTime.MIDNIGHT
        set(value) {
            field = value
            world.time = value.time.toLong()
        }

    lateinit var timeLeft: Time
        private set

    var turnCountdown: Int = -1
        private set

    var started: Boolean = false
        private set

    var turningIndex: Int = 0
        private set

    // ==================================================
    // =================== INIT BLOCK ===================
    // ==================================================

    fun init() {
        Bukkit.getScoreboardManager().mainScoreboard.teams.forEach { it.unregister() }

        initialPlayers = bukkitPlayers.map { NeoPlayer(it, this) }.shuffled()
        initialTarget = initialPlayers.toList().receiver()
        players = initialPlayers.toMutableList()

        arena = Arena(this)

        allowed.addAll(warp.allows)
        resetTurnCountdown()

        val playerCount = players.size
        for (i in 0..<playerCount) {
            val angle = 2 * Math.PI * i / playerCount
            players[i].teleport(
                Cord(warp.center.x + Constants.SPAWN_POINT_RADIUS * cos(angle), warp.center.y, warp.center.z + Constants.SPAWN_POINT_RADIUS * sin(angle)), world,
                Math.toDegrees(angle + Math.PI / 2).toFloat(), 0f // No, I am not a math guy...
            )
        }

        // Clear any previous block highlights.
        world.getEntitiesByClass(BlockDisplay::class.java).forEach { it.remove() }

        GameManager.add(this)
        info("Initialized the game.")
    }

    // ==================================================
    // ============== ABSTRACT/OPEN MEMBERS =============
    // ==================================================

    /**
     * This is called after the placement and usability were validated.
     * @return `true` if the turn should be skipped, `false` otherwise.
     */
    open fun preTurn(turn: Turn<*>): Boolean {
        return false
    }

    /**
     * This is executed right before [Turn.apply] is called.
     * If the turn is skipped, this will never be executed.
     * @return `true` if [Turn.apply] should be called, `false` otherwise.
     */
    open fun executeTurn(turn: Turn<*>): Boolean {
        return true
    }

    /**
     * This is called after the turn finished.
     * It will be called no matter if the turn got skipped or not.
     * @param skipped If this turn was skipped.
     */
    open fun postTurn(turn: Turn<*>, skipped: Boolean) {}

    /**
     * This is called after the `init {}` block finished.
     */
    open fun extraStart() {}

    /**
     * This is called after the [end] method finished.
     * @param force If the game was stopped forcefully.
     */
    open fun extraEnd(force: Boolean) {}

    /**
     * This is called after the [tick] method finished.
     * @param tick The tick which this method was called for.
     */
    open fun extraTick(tick: Ticking.Tick) {}

    open fun onOutOfBounds(player: NeoPlayer) = eliminate(player)

    open fun allows(turn: Turn<*>): Boolean {
        return !difficulty.completeRules || turn is WarpTurn || allowed.any { it.isInstance(turn) }
    }

    // ==================================================
    // ============= CLOSED UTILITY METHODS =============
    // ==================================================

    /**
     * Verifies if the turn can be used and will return `true` if it is canceled and the turn method can exit safely, due to being outside the arena, for example.
     * @return `false` if the turn method should abort, `true` if it can continue.
     */
    fun outOfBounds(location: Location, event: Cancellable? = null): Boolean {
        if (!location.toCord().inBounds(warp.lowCorner, warp.highCorner)) {
            if (event != null)
                event.isCancelled = true
            return true
        }
        return false
    }

    fun moveToNextPlayer() {
        currentPlayer().player.isGlowing = false
        turningIndex = (turningIndex + 1) % players.size
        currentPlayer().player.isGlowing = true
    }

    fun turnCountdownTick() {
        if (--turnCountdown <= 0) {
            if (history.size >= players.size && history.subList(history.size - players.size, history.size).all { it is TTimeLimit }) {
                players.forEach { it.sendMessage(it.locale().component("gameplay.info.ending.too-slow", color = Colors.NEUTRAL)) }
                draw(false)
            } else {
                turn(TTimeLimit(Time(turnCountdown.toLong()), null, currentPlayer()))
            }
        }
    }

    fun resetTurnCountdown() {
        turnCountdown = (info.turnTimer * difficulty.timeFactor).toInt()
    }

    fun nextTurn(turn: Turn<*>) {
        history.add(turn)
        turn.player?.databaseInfo?.addTurn()

        arena.resetCurrentBlocks()
        resetTurnCountdown()
        turnScheduler.tick()

        players.forEach { it.inventory.sync() }

        if (turn.player != null) {
            if (turn.player.hasModifier(Modifiers.Player.EXTRA_TURN)) {
                turn.player.removeModifier(Modifiers.Player.EXTRA_TURN)
            } else {
                moveToNextPlayer()
            }

            if (turn.player.hasModifier(Modifiers.Player.Default.ATTACKED)) {
                eliminate(turn.player)
            }
        }

        info("${turn.player?.name()} used ${turn.namespace()}.")
    }

    // ==================================================
    // ============== CLOSED BASIC METHODS ==============
    // ==================================================

    fun target(onlyAlive: Boolean = true): MinecraftReceiver = if (onlyAlive) players.receiver() else initialTarget

    fun currentPlayer(): NeoPlayer {
        if (turningIndex >= players.size)
            turningIndex = 0
        return players[turningIndex]
    }

    fun nextPlayer(): NeoPlayer {
        return players[(turningIndex + 1) % players.size]
    }

    fun player(bukkitPlayer: Player, onlyAlive: Boolean = true): NeoPlayer? {
        for (player in (if (onlyAlive) players else initialPlayers)) {
            if (player.uuid() == bukkitPlayer.uniqueId)
                return player
        }
        return null
    }

    fun allShopDone(): Boolean = players.all { it.inventory.shopDone }

    fun info(msg: String) = NeoBB.LOG.info("[${Constants.GAME_LOG_PREFIX}$id] $msg")
    fun warn(msg: String) = NeoBB.LOG.warn("[${Constants.GAME_LOG_PREFIX}$id] $msg")

    // ==================================================
    // =============== CLOSED GAME LOGIC ================
    // ==================================================

    override fun tick(tick: Ticking.Tick) {
        if (!started) return

        if (tick.isSecond()) {
            timeLeft--
            if (timeLeft.get() == 0L)
                draw(false)

            turnCountdownTick()
        }

        extraTick(tick)
    }

    fun eliminate(target: MinecraftReceiver) {
        if (target.hasModifier(Modifiers.Player.REVIVE)) {
            target.removeModifier(Modifiers.Player.REVIVE)
            target.eachBukkitPlayer { it.playEffect(EntityEffect.PROTECTED_FROM_DEATH) }

            target.sendMessage("gameplay.info.revive.use", color = Colors.POSITIVE)
            info("${target.name()} has used a revive.")
            return
        }

        target.eachNeoPlayer { p ->
            players.remove(p)
            p.databaseInfo.deathAnimation.play(p)
            p.player.health = 0.0
        }

        bukkitRunLater(Time(1)) {
            if (players.size == 1) {
                win(players.first())
            }
            target.teleport(warp.center)
            target.eachBukkitPlayer { it.gameMode = GameMode.SPECTATOR }
        }

        info("${target.name()} got eliminated.")
    }

    fun turn(turn: Turn<*>?, event: Cancellable? = null) {
        if (turn == null) return

        val player = turn.player!!
        val nextPlayer = player.nextPlayer()
        val turnNamespace = turn.namespace()

        var allowed = false

        when {
            Constants.CHECK_WARP_CLASSES && !allows(turn) -> {
                notAllowed(turn, "${player.name()} can't use $turnNamespace in the current warp.", "gameplay.info.not_allowed.class", Utilities.clazz(turn::class).toTitleCase(), warp.name(player.locale()))
            }
            turn is WarpTurn && !hasModifier(Modifiers.Game.NO_WARP.name + "_" + turn.name) -> {
                notAllowed(turn, "${player.name()} can't use $turnNamespace right now.", "gameplay.info.not_allowed.warp_filter", turn.name(player.locale()))
            }
            outOfBounds(turn.location(), event) -> {
                notAllowed(turn, "${player.name()} tried using $turnNamespace out of bounds.", "gameplay.info.out_of_bounds")
            }
            turn.unusable() -> {
                notAllowed(turn, "${player.name()} used $turnNamespace incorrectly.", "gameplay.info.wrong_usage")
            }
            else -> allowed = true
        }

        if (turn is TTimeLimit)
            allowed = true

        if (!allowed) return

        if (turn.isDummy()) {
            history += turn
            return
        }

        val skipped = preTurn(turn)

        clearSlot(turn)

        if (!skipped && executeTurn(turn)) {
            turn.apply()

            if (turn !is TTimeLimit) {
                if (turn.damage != 0.0)
                    nextPlayer.damage(turn.damage, turn is WinConCategory)

                effectManager.applyEffects(turn)
                initialPlayers.forEach { it.sendMessage("gameplay.info.turn-used", player.name(), turn.name(it.locale()), color = Colors.EXTRA) }
            }
        }

        nextTurn(turn)
        postTurn(turn, skipped)
    }

    private fun notAllowed(turn: Turn<*>, msg: String, key: String, vararg variables: String?) {
        info(msg)
        turn.player?.sendMessage(turn.player.locale().component(key, *variables, color = Colors.NEGATIVE))
        turn.player?.opponents(false)?.forEach { it.sendMessage(it.locale().component("gameplay.info.wrong_usage.other", turn.player.name(), color = Colors.NEUTRAL)) }
        turn.player?.playSound(Sound.ENTITY_VILLAGER_NO)
        clearSlot(turn)
        nextTurn(turn)
    }

    private fun clearSlot(turn: Turn<*>) {
        if (turn !is TTimeLimit) {
            runCatching {
                if (turn is StructureTurn) {
                    turn.referenceStructure.materials.forEach { m, a -> turn.player?.inventory?.clearFirst { i, _ -> i.type == m && i.amount == a } }
                } else {
                    turn.player?.inventory?.clearSlot(turn.player.player.inventory.heldItemSlot)
                }
            }
        }
    }

    fun warp(warp: WarpTurn) {
        info("${warp.player?.name()} has warped from ${this.warp.name} to ${warp.name}.")

        this.warp = warp
        if (playedWarps.all { it.name != warp.name })
            this.playedWarps += warp

        allowed.clear()
        allowed.addAll(warp.allows)
        modifiers.removeIf { it is String && it.startsWith(Modifiers.Game.NO_WARP.name) }

        players.forEach { p ->
            p.removeModifier(Modifiers.Player.Default.DEFENDED)
            if (warp.temperature == WarpTurn.Temperature.COLD) {
                p.player.fireTicks = 0
                turnScheduler.remove(ScheduleID.BURN)
            }
        }
    }

    fun start() {
        timeLeft = Time((info.totalTime.get() * difficulty.timeFactor).toLong())
        timeLeft.allowNegatives = true
        resetTurnCountdown()

        info("Game has been started.")
        started = true

        players.forEach { it.start() }

        if (info.showIntro) {
            val currentPlayer = players.first()
            currentPlayer.showTitle(Title.title(currentPlayer.locale().component("gameplay.info.start.first.you", color = Colors.POSITIVE),
                currentPlayer.locale().component("gameplay.info.start.first-sub", color = Colors.POSITIVE)))
            currentPlayer.opponents(false).forEach { it.showTitle(Title.title(it.locale().component("gameplay.info.start.first", currentPlayer.name(), color = Colors.POSITIVE),
                it.locale().component("gameplay.info.start.first-sub", color = Colors.POSITIVE))) }
        }

        currentPlayer().player.isGlowing = true

        extraStart()

        bukkitRunTimer(20, 20) {
            if (id !in GameManager.GAMES) {
                it.cancel()
            } else {
                if (!players.contains(currentPlayer()))
                    moveToNextPlayer()
            }
        }
    }

    fun end(force: Boolean = false) {
        initialPlayers.forEach {
            it.clean()
            it.team.unregister()
        }
        GameManager.remove(this)
        effectManager.clearHighlight()

        if (force) {
            warn("Stopped the game forcefully.")
            extraEnd(true)
            return
        }

        bukkitRunLater(Time(Constants.GAME_CLEANUP_DELAY)) {
            if (Configuration.PRODUCTION) {
                target(false).sendMessage("gameplay.info.kick", color = Colors.NEUTRAL)
                Bukkit.getServer().shutdown()
            } else {
                time = MinecraftTime.DAY
                arena.clear()
            }
        }

        extraEnd(false)
        info("Stopped the game gracefully.")

        // Save replay if gracefully stopping the game.
        saveReplay()
    }

    fun win(player: NeoPlayer) {
        player.showTitle(Title.title(player.locale().component("gameplay.info.ending.win", color = Colors.POSITIVE),
            player.locale().component("gameplay.info.ending.win-sub", color = Colors.NEUTRAL)))
        player.opponents(false).forEach { it.showTitle(Title.title(it.locale().component("gameplay.info.ending.lose", color = Colors.NEGATIVE),
            it.locale().component("gameplay.info.ending.lose-sub", color = Colors.NEUTRAL))) }
        info("${player.name()} has won the game.")

        player.databaseInfo.winAnimation.play(player)

        end(false)
        if (difficulty.ranked)
            Rating.updateFFARankings(initialPlayers.map { it.databaseInfo }, player.databaseInfo)
    }

    fun draw(force: Boolean = false) {
        players.forEach { it.showTitle(Title.title(it.locale().component("gameplay.info.ending.draw", color = Colors.NEUTRAL),
            it.locale().component("gameplay.info.ending.draw-sub", color = Colors.EXTRA))) }
        info("The game resulted in a draw.")

        end(force)
        if (difficulty.ranked)
            Rating.updateFFARankings(initialPlayers.map { it.databaseInfo }, null)
    }
}
