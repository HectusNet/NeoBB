package net.hectus.neobb.event;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.DefaultGame;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.util.Cord;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        if (event.getTo().clone().subtract(0, 1, 0).getBlock().getType() == Material.MAGMA_BLOCK) {
            NeoPlayer player = GameManager.player(event.getPlayer(), true);
            if (player != null) player.game.eliminatePlayer(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        List<Player> available = availablePlayers();
        if (available.size() >= NeoBB.CONFIG.getInt("starting-players")) {
            try {
                new DefaultGame(true, player.getWorld(), available, Cord.ofLocation(player.getLocation()), Cord.ofLocation(player.getLocation()).add(new Cord(9, 0, 9)));
            } catch (ReflectiveOperationException e) {
                NeoBB.LOG.error("Couldn't start match!", e);
            }
        }
    }

    public static @NotNull List<Player> availablePlayers() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getGameMode() == GameMode.ADVENTURE && player.getInventory().isEmpty())
                .filter(player -> GameManager.player(player, false) == null)
                .collect(Collectors.toList());
    }
}
