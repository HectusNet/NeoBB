package net.hectus.neobb.player

import com.marcpg.libpg.data.modifiable.ModifiableImpl
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import io.papermc.paper.entity.TeleportFlag
import net.hectus.neobb.game.Game
import net.hectus.neobb.game.mode.PersonGame
import net.hectus.neobb.modes.shop.Shop
import net.hectus.util.component
import net.hectus.util.display.SimpleActionBar
import net.hectus.util.display.SimpleScoreboard
import net.hectus.util.following
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience.Single
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import java.util.*

class NeoPlayer(val player: Player, val game: Game): ModifiableImpl(), Target, Single {
    companion object {
        val SCOREBOARD_MANAGER = Bukkit.getScoreboardManager()
    }

    val shop: Shop
    var inventory: NeoInventory = NeoInventory(this)
    var databaseInfo: DatabaseInfo = DatabaseInfo(player.uniqueId)

    var luck: Int = 20 ; private set
    var health: Double = game.info.startingHealth ; private set
    var armor: Double = 0.0 ; private set

    val team: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("highlight-${player.uniqueId}")

    val scoreboard: SimpleScoreboard? = game.scoreboard?.invoke(this)
    val actionBar: SimpleActionBar? = game.actionBar?.invoke(this)

    init {
        clean()

        team.color(databaseInfo.outline)
        team.addEntity(player)

        try {
            val constructor = game.info.shop.constructors.firstOrNull { it.parameters.size == 1 } ?: throw ReflectiveOperationException("No primary constructor")
            shop = constructor.call(this)
            shop.syncedOpen()
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException(e)
        }

        scoreboard?.start()
        actionBar?.start()
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

    override fun sendMessage(key: String, vararg variables: String?, color: TextColor?, decoration: TextDecoration?) {
        player.sendMessage(player.locale().component(key, *variables, color = color, decoration = decoration))
    }

    fun nextPlayer(): NeoPlayer = game.players.following(this) ?: this

    fun opponents(onlyAlive: Boolean = true): MutableList<NeoPlayer> {
        return (if (onlyAlive) game.players else game.initialPlayers).filter { it != this }.toMutableList()
    }

    fun clean() {
        player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        player.closeInventory()
        player.inventory.clear()
        player.clearActivePotionEffects()
        player.gameMode = GameMode.SURVIVAL
        player.isGlowing = false
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
