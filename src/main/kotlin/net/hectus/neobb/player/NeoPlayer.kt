package net.hectus.neobb.player

import com.marcpg.libpg.display.*
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.game.Game
import net.hectus.neobb.game.mode.PersonGame
import net.hectus.neobb.modes.shop.Shop
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

class NeoPlayer(player: Player, val game: Game): PlayerMinecraftReceiver(player) {
    val shop: Shop
    var inventory: NeoInventory = NeoInventory(this)
    var databaseInfo: DatabaseInfo = DatabaseInfo(player.uniqueId)

    var luck: Int = 20

    var health: Double = game.info.startingHealth
        set(value) {
            field = value.coerceAtLeast(0.0)
            player.health = (field / game.info.startingHealth * 20).coerceIn(0.5, 20.0)

            if (health <= 0.0)
                game.eliminate(this)
        }

    var armor: Double = 0.0
        set(value) {
            field = value.coerceAtLeast(0.0)
        }

    val team: Team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("highlight-${player.uniqueId}")

    val simpleScoreboard: SimpleScoreboard? = game.scoreboard?.invoke(this)
    val simpleActionBar: SimpleActionBar? = game.actionBar?.invoke(this)

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
    }

    fun start() {
        simpleScoreboard?.start()
        simpleActionBar?.start()
    }

    fun targetPlayer(beam: Boolean = false): NeoPlayer = game.targetPlayer(this, beam) ?: this
    fun targetPlayerOrNull(beam: Boolean = false): NeoPlayer? = game.targetPlayer(this, beam)

    fun opponents(onlyAlive: Boolean = true): MutableList<NeoPlayer> {
        return (if (onlyAlive) game.players else game.initialPlayers).filter { it != this }.toMutableList()
    }

    fun clean(display: Boolean = false) {
        if (display) {
            simpleScoreboard?.stop()
            simpleActionBar?.stop()

            player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        }

        player.closeInventory()
        player.inventory.clear()
        player.clearActivePotionEffects()
        player.gameMode = GameMode.SURVIVAL
        player.isGlowing = false
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

        this.health = this.health - finalDamage
        if (finalDamage > 0.0)
            playSound(Sound.ENTITY_PLAYER_HURT, 0.5f)
    }

    fun heal(health: Double) {
        if (game is PersonGame && game.time == MinecraftTime.MIDNIGHT) return

        this.health = this.health - health
        if (health > 0.0)
            playSound(Sound.BLOCK_BEACON_ACTIVATE, 0.5f)
    }

    fun addArmor(armor: Double) {
        this.armor = this.armor + armor
    }
}
