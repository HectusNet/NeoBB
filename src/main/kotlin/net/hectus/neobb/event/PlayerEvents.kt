package net.hectus.neobb.event

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.marcpg.libpg.util.asString
import com.marcpg.libpg.util.extractNumber
import com.marcpg.libpg.util.toLocation
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.game.mode.DefaultGame
import net.hectus.neobb.modes.shop.DefaultShop
import net.hectus.neobb.util.Configuration
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.cancelEvent
import net.hectus.neobb.util.playerEventAction
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerEvents : Listener {
    // ========== INVENTORY EVENTS ==========
    @EventHandler(ignoreCancelled = true)
    fun onInventoryInteract(event: InventoryInteractEvent) {
        if (event.whoClicked !is Player) return
        cancelEvent(event, event.whoClicked as Player, true) {
            event.inventory.type == InventoryType.PLAYER
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked !is Player) return
        if (event.slotType == InventoryType.SlotType.QUICKBAR && event.click == ClickType.DROP) {
            playerEventAction(event.whoClicked as Player, false, { !it.game.started }, { p ->
                runCatching {
                    val line = event.currentItem?.lore()?.get(1) ?: return@runCatching
                    p.inventory.addCoins(line.asString().extractNumber())
                }
                p.inventory.clearSlot(event.slot)
                (p.shop as? DefaultShop)?.syncContent()
            })
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerAttemptPickupItem(event: PlayerAttemptPickupItemEvent) {
        event.isCancelled = true
    }

    // ========== MOVEMENT EVENTS ==========
    @EventHandler(ignoreCancelled = true)
    fun onPlayerJump(event: PlayerJumpEvent) {
        cancelEvent(event, event.player, false) { it.hasModifier(Modifiers.Player.NO_JUMP) }
    }


    // ========== CONNECTION EVENTS ==========
    @EventHandler(ignoreCancelled = true)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val available = Bukkit.getOnlinePlayers()
            .filter { it.gameMode == GameMode.ADVENTURE && it.inventory.isEmpty }
            .filter { GameManager.player(it, false) == null }

        if (Configuration.PRODUCTION && available.size >= Configuration.STARTING_PLAYERS) {
            DefaultGame(player.world, available)
        } else {
            player.teleport(Configuration.SPAWN_CORD.toLocation(player.world))
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        playerEventAction(event.player, false, { true }, { p ->
            if (p.game.started) {
                p.game.eliminate(p)
            } else {
                p.game.players.remove(p)
            }
        })
    }
}
