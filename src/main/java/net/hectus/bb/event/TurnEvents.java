package net.hectus.bb.event;

import net.hectus.bb.player.PlayerData;
import net.hectus.bb.game.GameManager;
import net.hectus.bb.game.ImproperTurnException;
import net.hectus.bb.game.TurnUnusableException;
import net.hectus.bb.turn.Turn;
import net.hectus.bb.turn.TurnData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TurnEvents implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        PlayerData player = Objects.requireNonNull(GameManager.getPlayerData(event.getPlayer()));
        try {
            player.game().turn(new TurnData(player, Turn.valueOf(event.getBlock().getType().name()), event.getBlock()));
            return;
        } catch (TurnUnusableException | ImproperTurnException e) {
            player.player().sendActionBar(Component.text(e.getMessage(), NamedTextColor.YELLOW));
        } catch (IllegalArgumentException ignored) {}

        event.setCancelled(true);
    }
}
