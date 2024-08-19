package net.hectus.neobb.event;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.DefaultGame;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class GameEvents implements Listener {
    public static int currentTick;

    @EventHandler
    public void onServerTickEnd(@NotNull ServerTickEndEvent event) {
        currentTick = event.getTickNumber();
        GameManager.GAMES.forEach(Game::gameTick);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        NeoPlayer player = GameManager.player(event.getPlayer(), true);
        if (player != null)
            player.game.arena.addBlock(event.getBlock());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        if (event.getTo().clone().subtract(0, 1, 0).getBlock().getType() == Material.MAGMA_BLOCK) {
            NeoPlayer player = GameManager.player(event.getPlayer(), true);
            if (player != null) player.game.eliminatePlayer(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJump(@NotNull PlayerJumpEvent event) {
        NeoPlayer player = GameManager.player(event.getPlayer(), true);
        if (player != null && player.hasModifier("no_jump"))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleEnter(@NotNull VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player player) {
            NeoPlayer neoPlayer = GameManager.player(player, true);
            if (neoPlayer != null) neoPlayer.removeModifier("boat_damage");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFromTo(@NotNull BlockFromToEvent event) { // Cancel fluid flow
        if (event.getBlock().getType() != Material.DRAGON_EGG)
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTargetLivingEntity(@NotNull EntityTargetLivingEntityEvent event) {
        event.setCancelled(true); // Cancel any hostile mob activity.
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        List<Player> available = availablePlayers();
        if (available.size() >= NeoBB.CONFIG.getInt("starting-players")) {
            new DefaultGame(true, player.getWorld(), available);
        }
    }

    public static @NotNull List<Player> availablePlayers() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getGameMode() == GameMode.ADVENTURE && player.getInventory().isEmpty())
                .filter(player -> GameManager.player(player, false) == null)
                .collect(Collectors.toList());
    }
}
