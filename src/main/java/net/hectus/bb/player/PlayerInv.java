package net.hectus.bb.player;

import com.marcpg.libpg.lang.Translatable;
import com.marcpg.libpg.lang.Translation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PlayerInv {
    private final PlayerData player;
    private int coins = 40;
    private final List<ItemStack> regularHotbar = new ArrayList<>();
    private final List<ItemStack> overtimeHotbar = new ArrayList<>();
    private Hotbar currentHotbar = Hotbar.REGULAR;

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

    public Hotbar currentHotbar() {
        return currentHotbar;
    }

    public List<ItemStack> getHotbarContents(Hotbar hotbar) {
        return hotbar == Hotbar.OVERTIME ? overtimeHotbar : regularHotbar;
    }

    public List<ItemStack> getCurrentHotbarContents() {
        return getHotbarContents(currentHotbar);
    }

    public void addItem(Hotbar hotbar, ItemStack item) {
        List<ItemStack> items = getHotbarContents(hotbar);
        if (items.size() >= 9)
            throw new IndexOutOfBoundsException("Hotbar already full!");
        items.add(item);
        updateYou();
        player.opponent().inv().updateOpponent();
    }

    public void removeItem(@Range(from = 0, to = 8) int index, Hotbar hotbar) {
        getHotbarContents(hotbar).remove(index);
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
                inv.setItem(i + 27, items.get(i));
            } else {
                inv.clear(i + 27);
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
