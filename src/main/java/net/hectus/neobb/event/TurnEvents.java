package net.hectus.neobb.event;

import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.TCauldron;
import net.hectus.neobb.turn.TIronTrapdoor;
import net.hectus.neobb.turn.TPurpleWool;
import net.hectus.neobb.turn.TSpruceTrapdoor;
import net.hectus.neobb.util.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class TurnEvents implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.BLACK_STAINED_GLASS_PANE)
            event.setCancelled(true);

        NeoPlayer player = GameManager.player(event.getPlayer(), true);

        if (player == null) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) // Allow builders and admins to place blocks.
                event.setCancelled(true);
            return;
        }

        if (!player.game.started() || player.game.currentPlayer() != player) {
            player.sendMessage(Component.text("You cannot place blocks right now!", Colors.NEGATIVE));
            event.setCancelled(true);
            return;
        }

        Block block = event.getBlock();
        switch (event.getBlock().getType()) {
            case CAULDRON -> player.game.turn(new TCauldron(block, player));
            case IRON_TRAPDOOR -> player.game.turn(new TIronTrapdoor(block, player));
            case PURPLE_WOOL -> player.game.turn(new TPurpleWool(block, player));
            case SPRUCE_TRAPDOOR -> player.game.turn(new TSpruceTrapdoor(block, player));
            default -> event.setCancelled(true);
        }
    }
}
