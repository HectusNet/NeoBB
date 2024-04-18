package net.hectus.bb.player;

import com.marcpg.libpg.lang.Translatable;
import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.shop.PreGameShop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class PlayerInv {
    private final PlayerData player;
    private int coins = 40;
    private boolean shopDone = false;
    private Hotbar currentHotbar = Hotbar.REGULAR;
    private final List<ItemStack> regularHotbar = new ArrayList<>();
    private final List<ItemStack> overtimeHotbar = new ArrayList<>();

    public PlayerInv(PlayerData player) {
        this.player = player;
    }

    public PlayerData player() {
        return player;
    }

    public int coins() {
        return coins;
    }

    public boolean removeCoins(int amount) {
        if (coins < amount) return false;
        coins -= amount;
        return true;
    }

    public void setShopDone(boolean shopDone) {
        this.shopDone = shopDone;
    }

    public boolean shopDone() {
        return shopDone;
    }

    public Hotbar currentHotbar() {
        return currentHotbar;
    }

    public List<ItemStack> getHotbarContents(Hotbar hotbar) {
        return hotbar == Hotbar.OVERTIME ? overtimeHotbar : regularHotbar;
    }

    public List<ItemStack> getCurrentHotbarContents() {
        return getHotbarContents(currentHotbar);
    }

    public void setCurrentHotbarContents(List<ItemStack> items) {
        getCurrentHotbarContents().clear();
        getCurrentHotbarContents().addAll(items);
    }

    public void addItem(Hotbar hotbar, ItemStack item) {
        List<ItemStack> items = getHotbarContents(hotbar);
        if (items.size() >= 9)
            throw new IndexOutOfBoundsException("Hotbar already full!");
        items.add(item);
        updateYou();
        player.opponent().inv().updateOpponent();
        if (items.size() >= 9)
            currentDone();
    }

    public void currentDone() {
        PlayerInv oppInv = player.opponent().inv();
        shopDone = true;
        if (oppInv.shopDone()) {
            if (currentHotbar == Hotbar.REGULAR) {
                oppInv.shopDone = false;
                oppInv.currentHotbar = Hotbar.OVERTIME;
                PreGameShop.menu(oppInv.player);

                shopDone = false;
                currentHotbar = Hotbar.OVERTIME;
                PreGameShop.menu(player);

                System.out.println("Done!");
            } else {
                player.game().startMainGame();
            }
        }
    }

    public void removeItem(@Range(from = 0, to = 8) int index, Hotbar hotbar) {
        getHotbarContents(hotbar).remove(index);
    }

    public void removeItem(ItemStack item, Hotbar hotbar) {
        getHotbarContents(hotbar).remove(item);
    }

    public void enterOvertime() {
        this.currentHotbar = Hotbar.OVERTIME;
    }

    public void updateYou() {
        PlayerInventory inv = player.player().getInventory();
        List<ItemStack> items = getCurrentHotbarContents();
        for (int i = 0; i < 9; i++) {
            if (i < items.size()) {
                inv.setItem(i, items.get(i));
            } else {
                inv.clear(i);
            }
        }
    }

    public void updateOpponent() {
        PlayerInventory inv = player.player().getInventory();
        List<ItemStack> items = player.opponent().inv().getCurrentHotbarContents();
        for (int i = 0; i < 9; i++) {
            if (i < items.size()) {
                inv.setItem(i + 18, items.get(i));
            } else {
                inv.clear(i + 18);
            }
        }
    }

    public enum Hotbar implements Translatable {
        BOTH, REGULAR, OVERTIME;

        public Hotbar getOpposite() {
            return this == BOTH ? BOTH : (this == REGULAR ? OVERTIME : REGULAR);
        }

        @Override
        public String getTranslated(Locale locale) {
            return Translation.string(locale, "info.hotbar." + name().toLowerCase());
        }

        @Override
        public @NotNull Component getTranslatedComponent(Locale locale) {
            return Translation.component(locale, "info.hotbar." + name().toLowerCase())
                    .color(this == BOTH ? NamedTextColor.GREEN : (this == REGULAR ? NamedTextColor.RED : NamedTextColor.LIGHT_PURPLE));
        }
    }
}
