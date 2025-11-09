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
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.StructureTurn
import net.hectus.neobb.modes.turn.default_game.TDefaultWarp
import net.hectus.neobb.modes.turn.default_game.TTimeLimit
import net.hectus.neobb.modes.turn.default_game.WarpTurn
import net.hectus.neobb.modes.turn.default_game.attribute.TurnClazz
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

abstract class Game(val world: World, private val bukkitPlayers: List<Player>, val difficulty: GameDifficulty = Constants.DEFAULT_DIFFICULTY): ModifiableImpl(), Ticking {
    val id: String = Randomizer.generateRandomString(Constants.GAME_ID_LENGTH, Constants.GAME_ID_CHARSET)
    lateinit var arena: Arena
    val effectManager = EffectManager()
    val turnScheduler = TurnScheduler()

    lateinit var initialPlayers: List<NeoPlayer> private set
    private lateinit var initialTarget: ForwardingMinecraftReceiver
    lateinit var players: MutableList<NeoPlayer> private set
    val history: MutableList<TurnExec<*>> = mutableListOf()

    val allowed: MutableSet<TurnClazz> = mutableSetOf()

    abstract val info: GameInfo

    open val scoreboard: ((NeoPlayer) -> SimpleScoreboard)? = null
    open val actionBar: ((NeoPlayer) -> SimpleActionBar)? = null

    // ==================================================
    // ============ PUBLIC GETTER VARIABLES =============
    // ==================================================

    var warp: WarpTurn = TDefaultWarp
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

    open fun init() {
        Bukkit.getScoreboardManager().mainScoreboard.teams.forEach { it.unregister() }

        initialPlayers = bukkitPlayers.map { NeoPlayer(it, this) }.shuffled()
        initialTarget = initialPlayers.toList().receiver()
        players = initialPlayers.toMutableList()

        arena = Arena(this)

        allowed.addAll(warp.allows)
        resetTurnCountdown()

        val playerCount = players.size
        for (i in 0 until playerCount) {
            val angle = 2 * Math.PI * i / playerCount
            players[i].teleport(
                cord = warp.bounds.center2D + Cord(Constants.SPAWN_POINT_RADIUS * cos(angle), 0.0, Constants.SPAWN_POINT_RADIUS * sin(angle)),
                world = world,
                yaw = Math.toDegrees(angle + Math.PI / 2).toFloat(), // No, I am not a math guy...
                pitch = 0f,
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
    open fun preTurn(exec: TurnExec<*>): Boolean {
        return false
    }

    /**
     * This is executed right before [Turn.apply] is called.
     * If the turn is skipped, this will never be executed.
     * @return `true` if [Turn.apply] should be called, `false` otherwise.
     */
    open fun executeTurn(exec: TurnExec<*>): Boolean {
        return true
    }

    /**
     * This is called after the turn finished.
     * It will be called no matter if the turn got skipped or not.
     * @param skipped If this turn was skipped.
     */
    open fun postTurn(exec: TurnExec<*>, skipped: Boolean) {}

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
        return turn.clazz == null || turn.clazz == TurnClazz.NEUTRAL || !difficulty.completeRules || turn is WarpTurn || turn.clazz in allowed
    }

    open fun targetPlayer(player: NeoPlayer, beam: Boolean = false): NeoPlayer? = players.following(player)

    // ==================================================
    // ============= CLOSED UTILITY METHODS =============
    // ==================================================

    /**
     * Verifies if the turn can be used and will return `true` if it is canceled and the turn method can exit safely, due to being outside the arena, for example.
     * @return `false` if the turn method should abort, `true` if it can continue.
     */
    fun outOfBounds(cord: Cord, event: Cancellable? = null): Boolean {
        if (cord !in warp.bounds) {
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
        if (--turnCountdown == 0) {
            if (history.size >= players.size && history.subList(history.size - players.size, history.size).all { it.turn === TTimeLimit }) {
                players.forEach { it.sendMessage(it.locale().component("gameplay.info.ending.too-slow", color = Colors.NEUTRAL)) }
                draw(false)
            } else {
                turn(TurnExec(TTimeLimit, currentPlayer(), null, Time(turnCountdown.toLong())))
            }
        }
    }

    fun resetTurnCountdown() {
        turnCountdown = (info.turnTimer * difficulty.timeFactor).toInt()
    }

    fun nextTurn(exec: TurnExec<*>) {
        history += exec
        exec.player.databaseInfo.addTurn()

        arena.resetCurrentBlocks()
        resetTurnCountdown()
        turnScheduler.tick()

        players.forEach { it.inventory.sync() }

        if (exec.player.hasModifier(Modifiers.Player.EXTRA_TURN)) {
            exec.player.removeModifier(Modifiers.Player.EXTRA_TURN)
        } else {
            moveToNextPlayer()
        }

        if (exec.player.hasModifier(Modifiers.Player.Default.ATTACKED)) {
            eliminate(exec.player)
        }

        info("${exec.player.name()} used ${exec.turn.namespace}.")
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

    fun nextPlayer(): NeoPlayer = players[(turningIndex + 1) % players.size]

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
    fun warn(msg: String, e: Throwable) = NeoBB.LOG.warn("[${Constants.GAME_LOG_PREFIX}$id] $msg", e)

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
            target.teleport(warp.bounds.center2D)
            target.eachBukkitPlayer { it.gameMode = GameMode.SPECTATOR }
        }

        info("${target.name()} got eliminated.")
    }

    fun <T> turn(exec: TurnExec<T>, event: Cancellable? = null) {
        val player = exec.player
        val nextPlayer = player.targetPlayer()
        val turn = exec.turn

        var allowed = false

        when {
            Constants.CHECK_WARP_CLASSES && !allows(turn) -> {
                notAllowed(exec, "${player.name()} can't use ${turn.name} in the current warp.", "gameplay.info.not_allowed.class", turn.clazz?.name?.toTitleCase() ?: "Unknown", warp.name)
            }
            turn is WarpTurn && hasModifier(Modifiers.Game.NO_WARP.name + "_" + turn.namespace) -> {
                notAllowed(exec, "${player.name()} can't use ${turn.name} right now.", "gameplay.info.not_allowed.warp_filter", turn.name)
            }
            outOfBounds(exec.cord ?: warp.bounds.center3D, event) -> {
                notAllowed(exec, "${player.name()} tried using ${turn.name} out of bounds.", "gameplay.info.out_of_bounds")
            }
            turn.unusable(exec.player) -> {
                notAllowed(exec, "${player.name()} used ${turn.name} incorrectly.", "gameplay.info.wrong_usage")
            }
            else -> allowed = true
        }

        if (turn === TTimeLimit)
            allowed = true

        if (!allowed) return

        if (turn.isCombo) {
            history += exec
            clearSlot(exec)
            return
        }

        val skipped = preTurn(exec)

        clearSlot(exec)

        if (!skipped && executeTurn(exec)) {
            turn.apply(exec)

            if (turn !== TTimeLimit) {
                if (turn.damage != null && turn.damage != 0.0)
                    nextPlayer.damage(turn.damage!!, turn is WinConCategory)

                effectManager.applyEffects(exec)
                initialPlayers.forEach { it.sendMessage("gameplay.info.turn-used", player.name(), turn.translation(it.locale()), color = Colors.EXTRA) }
            }
        }

        nextTurn(exec)
        postTurn(exec, skipped)
    }

    private fun notAllowed(exec: TurnExec<*>, msg: String, key: String, vararg variables: String?) {
        info(msg)
        exec.player.sendMessage(exec.player.locale().component(key, *variables, color = Colors.NEGATIVE))
        exec.player.opponents(false).forEach { it.sendMessage(it.locale().component("gameplay.info.wrong_usage.other", exec.player.name(), color = Colors.NEUTRAL)) }
        exec.player.playSound(Sound.ENTITY_VILLAGER_NO)

        clearSlot(exec)
        nextTurn(exec)
    }

    private fun clearSlot(exec: TurnExec<*>) {
        if (exec.turn !== TTimeLimit) {
            runCatching {
                if (exec.turn is StructureTurn) {
                    exec.turn.referenceStructure.materials.forEach { (m, a) -> exec.player.inventory.clearFirst { i, _ -> i.type == m && i.amount == a } }
                } else {
                    exec.player.inventory.clearSlot(exec.player.player.inventory.heldItemSlot)
                }
            }
        }
    }

    fun warp(player: NeoPlayer, warp: WarpTurn) {
        info("${player.name()} has warped from the ${this.warp.name} to the ${warp.name}.")

        players.forEach { p -> p.sendMessage("gameplay.info.warp.used", this.warp.translation(p.locale()), warp.translation(p.locale()), color = Colors.EXTRA) }

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

            if (difficulty.completeRules) {
                p.sendMessage("gameplay.info.warp.playable", this.warp.translation(p.locale()), warp.translation(p.locale()), color = Colors.NEUTRAL)
                warp.allows.forEach { p.sendMessage(it.line(p.locale())) }
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
            it.clear(display = true)
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
        runCatching {
            saveReplay()
        }.onFailure {
            warn("Unable to save replay due to internal error: ${it.message}")
        }
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
