package net.hectus.neobb.event

import com.destroystokyo.paper.event.server.ServerTickEndEvent
import com.marcpg.libpg.util.bukkitRun
import com.marcpg.libpg.util.bukkitRunLater
import net.hectus.neobb.NeoBB
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.game.mode.HectusGame
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.Ticking
import net.hectus.neobb.util.playerEventAction
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.vehicle.VehicleEnterEvent

class GameEvents : Listener {
    companion object {
        private val DISABLED_DAMAGE = listOf(DamageCause.FIRE_TICK, DamageCause.FREEZE, DamageCause.POISON)
    }

    @EventHandler
    fun onServerTickEnd(event: ServerTickEndEvent) {
        runCatching {
            GameManager.GAMES.values.forEach { it.tick(Ticking.Tick(event.tickNumber)) }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        playerEventAction(event.player, false) { p ->
            if (event.player.openInventory.topInventory.type == InventoryType.CHEST) {
                event.isCancelled = true
                p.inventory.sellOne(event.itemDrop.itemStack)
                bukkitRun { p.inventory.sync() }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) {
        playerEventAction(event.player, true) { p ->
            if (p.hasModifier(Modifiers.Player.NO_MOVE) && event.hasChangedPosition()) {
                event.isCancelled = true
                return@playerEventAction
            }

            if (p.game is HectusGame && event.to.clone().subtract(0.0, 0.1, 0.0).block.type == Material.MAGMA_BLOCK) {
                p.game.eliminate(p)
            } else if (event.hasChangedPosition() && p.game.outOfBounds(p.location(), event)) {
                p.game.onOutOfBounds(p)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onVehicleEnter(event: VehicleEnterEvent) {
        if (event.entered is Player) {
            playerEventAction(event.entered as Player, true) { p ->
                p.removeModifier(Modifiers.Player.Default.BOAT_DAMAGE)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        playerEventAction(event.player, true) { p ->
            bukkitRunLater(10) {
                if (p.game.players.contains(p)) { // Ensure the death is not from the animation.
                    p.game.eliminate(p)
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun onBlockPlace(event: BlockPlaceEvent) {
        var game = false
        playerEventAction(event.player, true) { p ->
            try {
                p.game.arena.addBlock(event.block)
            } catch (e: IndexOutOfBoundsException) {
                NeoBB.LOG.warn("Placed blocks out of bounds: ${e.message}")
                event.isCancelled = true
            }
            game = true
        }
        if (!game && event.player.gameMode != GameMode.CREATIVE)
            event.isCancelled = true
    }

    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (!event.player.gameMode.isInvulnerable)
            event.isCancelled = true
    }


    @EventHandler(ignoreCancelled = true)
    fun onBlockFromTo(event: BlockFromToEvent) {
        if (event.block.isLiquid)
            event.isCancelled = true
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onEntityTargetLivingEntity(event: EntityTargetLivingEntityEvent) {
        event.isCancelled = true // Cancel any hostile mob activity.
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityDamage(event: EntityDamageEvent) {
        if (DISABLED_DAMAGE.contains(event.cause))
            event.isCancelled = true
    }
}
