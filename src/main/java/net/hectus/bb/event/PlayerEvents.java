package net.hectus.bb.event;

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.hectus.bb.player.PlayerData;
import net.hectus.bb.game.Game;
import net.hectus.bb.game.GameManager;
import net.hectus.bb.turn.effective.Burning;
import net.hectus.bb.warp.Warp;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerEvents implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByBlock(@NotNull EntityDamageByBlockEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR && event.getEntity() instanceof Player player) {
            Game game = GameManager.getGame(player);
            if (game != null)
                game.lose(GameManager.getPlayerData(player));

            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInventorySlotChange(@NotNull PlayerInventorySlotChangeEvent event) {
        if (!event.getOldItemStack().isEmpty() && event.getNewItemStack().isEmpty()) {
            PlayerData player = GameManager.getPlayerData(event.getPlayer());
            if (player != null) {
                int slot = event.getSlot();
                if (slot <= 8) {
                    player.inv().removeItem(slot, player.inv().currentHotbar());
                } else if (slot >= 27 && slot <= 35) {
                    player.inv().removeItem(slot - 27, player.inv().currentHotbar().getOpposite());
                }
            }
        }
    }
}
