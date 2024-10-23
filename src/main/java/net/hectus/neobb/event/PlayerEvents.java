package net.hectus.neobb.event;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.mode.DefaultGame;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.turn.default_game.warp.TDefaultWarp;
import net.hectus.neobb.util.Modifiers;
import net.hectus.neobb.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlayerEvents implements Listener {

    // ========== INVENTORY EVENTS ==========

    @EventHandler(ignoreCancelled = true)
    public void onInventoryInteract(@NotNull InventoryInteractEvent event) {
        if (event.getWhoClicked() instanceof Player eventPlayer)
            Utilities.cancelEvent(event, eventPlayer, true, p -> event.getInventory().getType() == InventoryType.PLAYER);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.QUICKBAR && event.getClick() == ClickType.DROP && event.getWhoClicked() instanceof Player eventPlayer)
            Utilities.playerEventAction(eventPlayer, false, p -> !p.game.started(), p -> {
                try { // Ignore this hell of code please lol.
                    Component line = Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).lore()).get(1);
                    if (line == null) return;
                    int cost = Utilities.extractNumber(PlainTextComponentSerializer.plainText().serialize(line));
                    p.inventory.addCoins(cost);
                } catch (Exception ignored) {}
                p.inventory.setDeckSlot(event.getSlot(), null, null);
            });
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerAttemptPickupItem(@NotNull PlayerAttemptPickupItemEvent event) {
        // Just enabling keep inventory would work too, but would get rid of the cool effect when a player dies.
        event.setCancelled(true);
    }

    // ========== MOVEMENT EVENTS ==========

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJump(@NotNull PlayerJumpEvent event) {
        Utilities.cancelEvent(event, event.getPlayer(), false, p -> p.hasModifier(Modifiers.P_NO_JUMP));
    }

    // ========== CONNECTION EVENTS ==========

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        List<Player> available = Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getGameMode() == GameMode.ADVENTURE && p.getInventory().isEmpty())
                .filter(p -> GameManager.player(p, false) == null)
                .collect(Collectors.toList());

        if (NeoBB.PRODUCTION && available.size() >= NeoBB.CONFIG.getInt("starting-players")) {
            new DefaultGame(true, player.getWorld(), available);
        } else {
            player.teleport(new TDefaultWarp(player.getWorld()).location().clone());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        Utilities.playerEventAction(event.getPlayer(), false, p -> true, p -> {
            if (p.game.started()) {
                p.game.eliminatePlayer(p);
            } else {
                p.game.players().remove(p);
                p.game.initialPlayers().remove(p);
            }
        });
    }
}
