package net.hectus.neobb.event;

import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.*;
import net.hectus.neobb.turn.default_game.item.TChorusFruit;
import net.hectus.neobb.turn.default_game.item.TIronShovel;
import net.hectus.neobb.util.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("DataFlowIssue")
public class TurnEvents implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.BLACK_STAINED_GLASS_PANE)
            event.setCancelled(true);

        NeoPlayer player = GameManager.player(event.getPlayer(), true);
        if (unusable(event.getPlayer(), player, event, "You cannot place blocks right now!")) return;

        Block block = event.getBlock();
        switch (event.getBlock().getType()) {
            case BLACK_WOOL -> player.game.turn(new TBlackWool(block, player));
            case CAULDRON -> player.game.turn(new TCauldron(block, player));
            case CYAN_CARPET -> player.game.turn(new TCyanCarpet(block, player));
            case FIRE -> player.game.turn(new TFire(block, player));
            case GOLD_BLOCK -> player.game.turn(new TGoldBlock(block, player));
            case GREEN_CARPET -> player.game.turn(new TGreenCarpet(block, player));
            case IRON_TRAPDOOR -> player.game.turn(new TIronTrapdoor(block, player));
            case MAGENTA_GLAZED_TERRACOTTA -> player.game.turn(new TMagentaGlazedTerracotta(block, player));
            case MAGMA_BLOCK -> player.game.turn(new TMagmaBlock(block, player));
            case NETHERRACK -> player.game.turn(new TNetherrack(block, player));
            case PURPLE_WOOL -> player.game.turn(new TPurpleWool(block, player));
            case SCULK -> player.game.turn(new TSculk(block, player));
            case SPONGE -> player.game.turn(new TSponge(block, player));
            case SPRUCE_TRAPDOOR -> player.game.turn(new TSpruceTrapdoor(block, player));
            case STONECUTTER -> player.game.turn(new TStonecutter(block, player));
            default -> event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(@NotNull PlayerItemConsumeEvent event) {
        NeoPlayer player = GameManager.player(event.getPlayer(), true);
        if (unusable(event.getPlayer(), player, event, "You cannot consume items right now!")) return;

        if (event.getItem().getType() == Material.CHORUS_FRUIT) {
            player.game.turn(new TChorusFruit(event.getItem(), event.getPlayer().getLocation(), player));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
        NeoPlayer player = GameManager.player(event.getPlayer(), true);
        if (unusable(event.getPlayer(), player, event, "You cannot drop items right now!")) return;

        if (event.getItemDrop().getItemStack().getType() == Material.IRON_SHOVEL) {
            player.game.turn(new TIronShovel(event.getItemDrop().getItemStack(), event.getItemDrop().getLocation(), player));
        }
    }

    private boolean unusable(Player fallbackPlayer, NeoPlayer player, Cancellable event, String message) {
        if (player == null) {
            if (fallbackPlayer.getGameMode() != GameMode.CREATIVE) // Allow builders and admins to do this.
                event.setCancelled(true);
            return true;
        }

        if (!player.game.started() || player.game.currentPlayer() != player) {
            player.sendMessage(Component.text(message, Colors.NEGATIVE));
            event.setCancelled(true);
            return true;
        }
        return false;
    }
}
