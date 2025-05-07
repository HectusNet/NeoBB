package net.hectus.neobb.player

import com.marcpg.libpg.data.modifiable.ModifiableImpl
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import io.papermc.paper.entity.TeleportFlag
import io.papermc.paper.scoreboard.numbers.NumberFormat
import net.hectus.neobb.game.Game
import net.hectus.neobb.game.mode.PersonGame
import net.hectus.neobb.shop.Shop
import net.hectus.neobb.util.Ticking
import net.hectus.neobb.util.following
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience.Single
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import java.util.*

class NeoPlayer(val player: Player, val game: Game): ModifiableImpl(), Target, Single, Ticking {
    companion object {
        val SCOREBOARD_MANAGER = Bukkit.getScoreboardManager()
    }

    val shop: Shop
    var inventory: NeoInventory = NeoInventory(this)
    var databaseInfo: DatabaseInfo = DatabaseInfo(player.uniqueId)

    var luck: Int = 20 ; private set
    var health: Double = 0.0 ; private set
    var armor: Double = 0.0 ; private set

    init {
        clean()
        try {
            val constructor = game.info.shop.constructors.firstOrNull { it.parameters.size == 1 } ?: throw ReflectiveOperationException("No primary constructor")
            shop = constructor.call(this)
            shop.syncedOpen()
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException(e)
        }
    }

    override fun audience(): Audience = player

    override fun name(): String = player.name
    override fun uuid(): UUID = player.uniqueId
    override fun toString(): String = name()

    override fun location(): Location = player.location

    override fun playSound(sound: Sound, volume: Float) {
        player.playSound(player, sound, volume, 1.0f)
    }

    override fun teleport(cord: Cord, yaw: Float, pitch: Float) {
        teleport(Location(game.world, cord.x(), cord.y(), cord.z(), yaw, pitch))
    }

    override fun teleport(location: Location) {
        player.teleport(location, TeleportFlag.EntityState.RETAIN_OPEN_INVENTORY)
    }

    override fun closeInv() {
        player.closeInventory()
    }

    override fun eachBukkitPlayer(action: (Player) -> Unit) {
        action.invoke(player)
    }

    override fun eachNeoPlayer(action: (NeoPlayer) -> Unit) {
        action.invoke(this)
    }

    override fun tick(tick: Ticking.Tick) {
        val scoreboardText = game.scoreboard(this)
        if (!scoreboardText.isNullOrEmpty()) {
            val objective: Objective
            if (player.scoreboard == SCOREBOARD_MANAGER.mainScoreboard) {
                val scoreboard = SCOREBOARD_MANAGER.newScoreboard
                objective = scoreboard.registerNewObjective("neobb", Criteria.DUMMY, scoreboardText.first())
                objective.displaySlot = DisplaySlot.SIDEBAR
                player.scoreboard = scoreboard
            } else {
                objective = player.scoreboard.getObjective("neobb") ?: player.scoreboard.registerNewObjective("neobb", Criteria.DUMMY, scoreboardText.first())
            }

            scoreboardText.forEachIndexed { i, _ ->
                if (i == 0) return@forEachIndexed

                val score = objective.getScore("score-$i")
                score.numberFormat(NumberFormat.blank())
                score.score = scoreboardText.size - i
                score.customName(scoreboardText[i])
            }
        }

        val actionbarText = game.actionbar(this)
        if (actionbarText != null && actionbarText == Component.empty()) {
            player.sendActionBar(actionbarText)
        }
    }

    fun nextPlayer(): NeoPlayer = game.players.following(this)!!

    fun opponents(onlyAlive: Boolean = true): MutableList<NeoPlayer> {
        return (if (onlyAlive) game.players else game.initialPlayers).filter { it != this }.toMutableList()
    }

    fun clean() {
        player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        player.closeInventory()
        player.inventory.clear()
        player.clearActivePotionEffects()
        player.gameMode = GameMode.SURVIVAL
    }

    fun addLuck(luck: Int) {
        this.luck += luck
    }

    fun damage(damage: Double, raw: Boolean = false) {
        var finalDamage = damage

        if (!raw && armor > 0.0) {
            if (armor >= finalDamage) {
                armor -= finalDamage
            } else {
                finalDamage -= armor
                armor = 0.0
            }
            playSound(Sound.ENCHANT_THORNS_HIT)
        }

        health(health - finalDamage)
        if (finalDamage > 0.0)
            playSound(Sound.ENTITY_PLAYER_HURT, 0.5f)
    }

    fun heal(health: Double) {
        if (game is PersonGame && game.time == MinecraftTime.MIDNIGHT) return

        health(this.health - health)
        if (health > 0.0)
            playSound(Sound.BLOCK_BEACON_ACTIVATE, 0.5f)
    }

    fun health(health: Double) {
        this.health = health.coerceAtLeast(0.0)
        player.health = (this.health / game.info.startingHealth * 20).coerceIn(0.5, 20.0)

        if (health <= 0.0)
            game.eliminate(this)
    }

    fun addArmor(armor: Double) {
        armor(this.armor + armor)
    }

    fun armor(armor: Double) {
        this.armor = armor.coerceAtLeast(0.0)
    }
}
