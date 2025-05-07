package net.hectus.neobb.game

import com.marcpg.libpg.data.modifiable.ModifiableImpl
import com.marcpg.libpg.data.time.Time
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.NeoBB
import net.hectus.neobb.Rating
import net.hectus.neobb.cosmetic.EffectManager
import net.hectus.neobb.game.util.*
import net.hectus.neobb.player.ForwardingTarget
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.player.Target
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.TTimeLimit
import net.hectus.neobb.turn.default_game.warp.TDefaultWarp
import net.hectus.neobb.turn.default_game.warp.WarpTurn
import net.hectus.neobb.turn.person_game.categorization.WinConCategory
import net.hectus.neobb.util.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import kotlin.math.cos
import kotlin.math.sin
import kotlin.reflect.KClass

abstract class Game(val world: World, private val bukkitPlayers: List<Player>, val difficulty: GameDifficulty = GameDifficulty.NORMAL): ModifiableImpl(), Ticking {
    val id: String = Randomizer.generateRandomString(10, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
    lateinit var arena: Arena
    val effectManager = EffectManager()
    val turnScheduler = TurnScheduler()

    lateinit var initialPlayers: List<NeoPlayer> private set
    private lateinit var initialTarget: ForwardingTarget
    lateinit var players: MutableList<NeoPlayer> private set
    val history: MutableList<Turn<*>> = mutableListOf()

    val allowed: MutableSet<KClass<*>> = mutableSetOf()

    abstract val info: GameInfo

    // ==================================================
    // ============ PUBLIC GETTER VARIABLES =============
    // ==================================================

    var warp: WarpTurn = TDefaultWarp(null, null)
        set(warp) = warp(warp)

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
        initialPlayers = bukkitPlayers.map { NeoPlayer(it, this) }.toList()
        initialTarget = ForwardingTarget(initialPlayers.toMutableList())
        players = initialPlayers.toMutableList()

        arena = Arena(this)

        allowed.addAll(warp.allows)
        resetTurnCountdown()

        val radius = 2.5
        val playerCount = players.size

        for (i in 0..<playerCount) {
            val angle = 2 * Math.PI * i / playerCount
            players[i].teleport(
                Cord(warp.center.x() + radius * cos(angle), warp.center.y(), warp.center.z() + radius * sin(angle)),
                Math.toDegrees(angle + Math.PI / 2).toFloat(), 0f // No, I am not a math guy...
            )
        }

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

    open fun scoreboard(player: NeoPlayer): List<Component>? = null
    open fun actionbar(player: NeoPlayer): Component? = null

    open fun onOutOfBounds(player: NeoPlayer) = eliminate(player)

    open fun allows(turn: Turn<*>): Boolean {
        return allowed.all { it.isInstance(turn) && (turn !is WarpTurn || !hasModifier(Modifiers.Game.NO_WARP.name + "_" + turn.name)) }
    }

    // ==================================================
    // ============= CLOSED UTILITY METHODS =============
    // ==================================================

    /**
     * Verifies if the turn can be used and will return `true` if it is canceled and the turn method can exit safely, due to being outside the arena, for example.
     * @return `false` if the turn method should abort, `true` if it can continue.
     */
    fun outOfBounds(location: Location, event: Cancellable? = null): Boolean {
        if (!location.asCord().inBounds(warp.cord!!, warp.highCorner)) {
            if (event != null)
                event.isCancelled = true
            return true
        }
        return false
    }

    fun moveToNextPlayer() {
        turningIndex = (turningIndex + 1) % players.size
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

            if (turn.player.hasModifier(Modifiers.Player.Default.ATTACKED) && !turn.player.hasModifier(Modifiers.Player.Default.DEFENDED)) {
                eliminate(turn.player)
            }
        }

        info("${turn.player?.name()} used $turn.")
    }

    // ==================================================
    // ============== CLOSED BASIC METHODS ==============
    // ==================================================

    fun target(onlyAlive: Boolean = true): Target = if (onlyAlive) ForwardingTarget(players) else initialTarget

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

    fun info(msg: String) {
        NeoBB.LOG.info("[G-$id] $msg")
    }

    fun warn(msg: String) {
        NeoBB.LOG.warn("[G-$id] $msg")
    }

    // ==================================================
    // =============== CLOSED GAME LOGIC ================
    // ==================================================

    override fun tick(tick: Ticking.Tick) {
        if (!started) return

        players.forEach { it.tick(tick) }
        if (tick.isSecond()) {
            timeLeft.decrement()
            if (timeLeft.get() == 0L)
                draw(false)

            turnCountdownTick()
        }

        extraTick(tick)
    }

    fun eliminate(target: Target) {
        if (target.hasModifier(Modifiers.Player.REVIVE)) {
            target.removeModifier(Modifiers.Player.REVIVE)
            target.eachBukkitPlayer { it.playEffect(EntityEffect.PROTECTED_FROM_DEATH) }

            target.eachNeoPlayer { it.sendMessage(it.locale().component("gameplay.info.revive.use", color = Colors.POSITIVE)) }
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

    fun turn(turn: Turn<*>, event: Cancellable? = null) {
        val player = turn.player!!
        val turnNamespace = turn.namespace()

        if (outOfBounds(turn.location(), event)) {
            info("${player.name()} used $turnNamespace out of bounds.")
            return
        }

        if (turn.isDummy()) {
            history += turn
            return
        }

        if (difficulty.completeRules && turn.unusable()) {
            info("${player.name()} used $turnNamespace incorrectly.")
            player.sendMessage(player.locale().component("gameplay.info.wrong_usage", color = Colors.NEGATIVE))
            player.playSound(Sound.ENTITY_VILLAGER_NO)
            nextTurn(turn)
            return
        }

        val skipped = preTurn(turn)

        if (turn !is TTimeLimit) {
            runCatching {
                player.inventory.clearSlot(player.player.inventory.heldItemSlot)
            }
        }

        if (!skipped && executeTurn(turn)) {
            turn.apply()

            if (turn.damage != 0.0)
                player.damage(turn.damage, turn is WinConCategory)

            effectManager.applyEffects(turn)
            initialPlayers.forEach { it.sendMessage(it.locale().component("gameplay.info.turn-used", player.name(), turnNamespace, color = Colors.EXTRA)) }
        }

        nextTurn(turn)
        postTurn(turn, skipped)
    }

    fun warp(warp: WarpTurn) {
        info("${warp.player?.name()} has warped from ${this.warp.name} to ${warp.name}.")

        this.warp = warp
        allowed.clear()
        allowed.addAll(warp.allows)
        modifiers.removeIf { it is String && it.startsWith(Modifiers.Game.NO_WARP.name) }

        players.forEach { p ->
            if (warp.temperature == WarpTurn.Temperature.COLD) {
                p.player.fireTicks = 0
                turnScheduler.remove(ScheduleID.BURN)
            }
        }
    }

    fun start() {
        timeLeft = Time((info.totalTime.get() * difficulty.timeFactor).toLong())
        timeLeft.setAllowNegatives(true)
        resetTurnCountdown()

        info("Game has been started.")
        started = true

        if (info.showIntro) {
            val currentPlayer = players.first()
            currentPlayer.showTitle(Title.title(currentPlayer.locale().component("gameplay.info.start.first.you", color = Colors.POSITIVE),
                currentPlayer.locale().component("gameplay.info.start.first-sub", color = Colors.POSITIVE)))
            currentPlayer.opponents(false).forEach { it.showTitle(Title.title(it.locale().component("gameplay.info.start.first", currentPlayer.name(), color = Colors.POSITIVE),
                it.locale().component("gameplay.info.start.first-sub", color = Colors.POSITIVE))) }
        }

        extraStart()
    }

    fun end(force: Boolean = false) {
        initialPlayers.forEach { it.clean() }
        GameManager.remove(this)
        effectManager.clearHighlight()

        if (force) {
            warn("Stopped the game forcefully.")
            extraEnd(true)
            return
        }

        bukkitRunLater(Time(5)) {
            if (Configuration.PRODUCTION) {
                initialPlayers.forEach { it.player.kick(it.locale().component("gameplay.info.kick", color = Colors.NEUTRAL)) }
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
        if (difficulty.ranked) // TODO: Support multiple winners/losers!
            Rating.updateRankingsWin(player.databaseInfo, player.opponents(false).first().databaseInfo, difficulty.eloFactor)
    }

    fun draw(force: Boolean = false) {
        players.forEach { it.showTitle(Title.title(it.locale().component("gameplay.info.ending.draw", color = Colors.NEUTRAL),
            it.locale().component("gameplay.info.ending.draw-sub", color = Colors.EXTRA))) }
        info("The game resulted in a draw.")

        end(force)
        if (difficulty.ranked) // TODO: Support multiple players for draw!
            Rating.updateRankingsDraw(initialPlayers.first().databaseInfo, initialPlayers.last().databaseInfo, difficulty.eloFactor)
    }

    fun giveup(player: NeoPlayer) {
        players.forEach { it.showTitle(Title.title(it.locale().component("gameplay.info.ending.giveup", player.name(), color = Colors.NEUTRAL),
            it.locale().component("gameplay.info.ending.giveup-sub", player.name(), color = Colors.EXTRA))) }
        info("${player.name()} has given up.")

        player.opponents(true).forEach { it.databaseInfo.winAnimation.play(it) }

        end(false)
        if (difficulty.ranked) { // TODO: Support multiple players for draw!
            runCatching {
                Rating.updateRankingsWin(player.opponents(true).first().databaseInfo, player.databaseInfo, difficulty.eloFactor)
            }.onFailure {
                Rating.updateRankingsWin(player.opponents(false).first().databaseInfo, player.databaseInfo, difficulty.eloFactor)
            }
        }
    }
}
