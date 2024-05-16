package net.hectus.bb.event;

import com.marcpg.libpg.lang.Translation;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import net.hectus.bb.event.custom.PlayerWarpEvent;
import net.hectus.bb.event.custom.TurnDoneEvent;
import net.hectus.bb.game.Game;
import net.hectus.bb.game.GameManager;
import net.hectus.bb.player.PlayerData;
import net.hectus.bb.turn.Suggestions;
import net.hectus.bb.turn.effective.Burning;
import net.hectus.bb.warp.Warp;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PlayerEvents implements Listener {
    @EventHandler
    public void onPlayerWarp(@NotNull PlayerWarpEvent event) {
        event.to().teleport(event.game());
        event.game().players().forEach(p -> p.player().clearActivePotionEffects());
        if (event.to().temperature == Warp.Temperature.COLD) {
            event.player().specialEffects().removeIf(turnCounter -> turnCounter instanceof Burning);
        }
        event.game().players().forEach(p -> p.player().sendMessage(Translation.component(p.player().locale(), "gameplay.info.warp-used", event.player().player().getName(), event.from().getTranslated(p.player().locale()), event.to().getTranslated(p.player().locale())).color(NamedTextColor.YELLOW)));
    }

    @EventHandler
    public void onTurnDone(@NotNull TurnDoneEvent event) {
        PlayerData nowTurning = event.turnData().player().opponent();

        List<Material> suggestions = Suggestions.suggest(nowTurning, event.turnData());

        for (int i = 0; i < 9; i++) {
            ItemStack item = nowTurning.player().getInventory().getItem(i);
            if (item != null && !item.isEmpty() && suggestions.contains(item.getType())) {
                item.addUnsafeEnchantment(Enchantment.MENDING, 1);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByBlock(@NotNull EntityDamageByBlockEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR && event.getEntity() instanceof Player player) {
            Game game = GameManager.getGame(player);
            if (game != null) {
                game.lose(GameManager.getPlayerData(player));
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInventorySlotChange(@NotNull PlayerInventorySlotChangeEvent event) {
        if (!event.getOldItemStack().isEmpty() && event.getNewItemStack().isEmpty()) {
            PlayerData player = GameManager.getPlayerData(event.getPlayer());
            if (player != null && player.game().hasStarted() && event.getSlot() <= 8) {
                player.inv().removeItemInGame(event.getSlot());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof PlayerInventory && event.getSlotType() == InventoryType.SlotType.QUICKBAR) {
            PlayerData player = GameManager.getPlayerData((Player) event.getWhoClicked());
            if (player != null && !player.inv().shopDone()) {
                player.inv().removeItemInShop(event.getSlot());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) return;

        PlayerData p = GameManager.getPlayerData(event.getPlayer());
        if (p == null) return;

        if (p.modifiers.isEnabled("frozen") || (p != p.game().turning() && !p.modifiers.isEnabled("always_move")))
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(@NotNull BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
            event.setCancelled(true);
    }
}
