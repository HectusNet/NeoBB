package net.hectus.neobb.event;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.DefaultGame;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.util.Modifiers;
import net.hectus.neobb.util.Utilities;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.*;

public class GameEvents implements Listener {
    private static final List<EntityDamageEvent.DamageCause> DISABLED_DAMAGE = List.of(FIRE_TICK, FREEZE, POISON);

    // ========== SERVER EVENTS ==========

    @EventHandler
    public void onServerTickEnd(@NotNull ServerTickEndEvent event) {
        GameManager.GAMES.forEach(game -> game.gameTick(event.getTickNumber()));
    }

    // ========== PLAYER EVENTS ==========

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        Utilities.playerEventAction(event.getPlayer(), true, p -> true, p -> {
            // TODO: Re-add this and make it properly kill players when moving out of the arena, instead of just all the time.
//            if (event.hasChangedBlock()) {
//                if (!Cord.ofLocation(p.player.getLocation()).inBounds(Cord.ofLocation(p.game.warp().lowCorner()), Cord.ofLocation(p.game.warp().highCorner())))
//                    p.game.eliminatePlayer(p);
//            }

            if (p.hasModifier(Modifiers.P_NO_MOVE) && event.hasChangedPosition()) {
                event.setCancelled(true);
            } else if (p.game instanceof DefaultGame && event.getTo().clone().subtract(0, 1, 0).getBlock().getType() == Material.MAGMA_BLOCK) {
                p.game.eliminatePlayer(p);
            }
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleEnter(@NotNull VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player eventPlayer)
            Utilities.playerEventAction(eventPlayer, true, p -> true, p -> p.removeModifier(Modifiers.P_DEFAULT_BOAT_DAMAGE));
    }

    // ========== BLOCK EVENTS ==========

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        AtomicBoolean game = new AtomicBoolean(false);
        Utilities.playerEventAction(event.getPlayer(), true, p -> true, p -> {
            try {
                p.game.arena.addBlock(event.getBlock());
            } catch (IndexOutOfBoundsException e) {
                NeoBB.LOG.warn("Placed blocks out of bounds: {}", e.getMessage());
                event.setCancelled(true);
            }
            game.set(true);
        });
        if (!game.get() && event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFromTo(@NotNull BlockFromToEvent event) { // Cancel fluid flow
        if (event.getBlock().getType() != Material.DRAGON_EGG)
            event.setCancelled(true);
    }

    // ========== ENTITY EVENTS ==========

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityTargetLivingEntity(@NotNull EntityTargetLivingEntityEvent event) {
        event.setCancelled(true); // Cancel any hostile mob activity.
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(@NotNull EntityDamageEvent event) {
        if (DISABLED_DAMAGE.contains(event.getCause()))
            event.setCancelled(true);
    }
}
