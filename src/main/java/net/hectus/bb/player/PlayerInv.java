package net.hectus.bb.player;

import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.shop.ShopItemUtilities;
import net.hectus.bb.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;

public final class PlayerInv {
    private final PlayerData player;
    private int coins = 40;
    private boolean shopDone = false;
    private final List<ItemStack> hotbar = new ArrayList<>();

    public PlayerInv(PlayerData player) {
        this.player = player;
    }

    public int coins() {
        return coins;
    }

    public boolean removeCoins(int amount) {
        if (coins < amount) return false;
        coins -= amount;
        return true;
    }

    public boolean shopDone() {
        return shopDone;
    }

    public void currentDone() {
        PlayerInv oppInv = player.opponent().inv();
        if (oppInv.shopDone()) {
            oppInv.shopDone = false;
            player.game().startMainGame();
        } else {
            shopDone = true;
        }
    }

    public List<ItemStack> getContents() {
        return hotbar;
    }

    public void setContents(List<ItemStack> items) {
        hotbar.clear();
        hotbar.addAll(items);
    }

    public void addItem(ItemStack item) {
        if (hotbar.size() >= 9)
            throw new IndexOutOfBoundsException("Hotbar already full!");
        hotbar.add(item);
        fullUpdate();
        if (hotbar.size() >= 9)
            currentDone();
    }

    public void removeItemInGame(@Range(from = 0, to = 8) int index) {
        hotbar.remove(index);
    }

    public void removeItemInShop(@Range(from = 0, to = 8) int index) {
        coins += ShopItemUtilities.getPrice(hotbar.get(index).getType());
        hotbar.remove(index);
        fullUpdate();
    }

    public void removeItemInShop(@NotNull ItemStack item) {
        coins += ShopItemUtilities.getPrice(item.getType());
        hotbar.remove(item);
        fullUpdate();
    }

    private void fullUpdate() {
        updateYou();
        player.opponent().inv().updateOpponent();
    }

    public void updateYou() {
        PlayerInventory inv = player.player().getInventory();
        List<ItemStack> items = getContents();
        for (int i = 0; i < 9; i++) {
            if (i < items.size()) {
                inv.setItem(i, items.get(i));
            } else {
                inv.clear(i);
            }
        }
        inv.setItem(13, new ItemBuilder(Material.GOLD_NUGGET)
                .name(Translation.component(player.player().locale(), "item-lore.cost.value", coins))
                .build()
        );
    }

    public void updateOpponent() {
        PlayerInventory inv = player.player().getInventory();
        List<ItemStack> items = player.opponent().inv().getContents();
        for (int i = 0; i < 9; i++) {
            if (i < items.size()) {
                inv.setItem(i + 18, items.get(i));
            } else {
                inv.clear(i + 18);
            }
        }
    }
}
