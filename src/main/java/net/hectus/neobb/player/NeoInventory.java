package net.hectus.neobb.player;

import net.hectus.neobb.turn.Turn;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NeoInventory {
    private final NeoPlayer player;
    private int coins;
    private ItemStack[] deck;
    private boolean shopDone;

    public NeoInventory(@NotNull NeoPlayer player) {
        this.player = player;
        this.coins = player.game.info().coins();
        this.deck = new ItemStack[player.game.info().deckSize()];
        sync();
    }

    public NeoPlayer player() {
        return player;
    }

    public int coins() {
        return coins;
    }

    public synchronized void setCoins(int coins) {
        this.coins = coins;
    }

    public synchronized boolean removeCoins(int coins) {
        if (this.coins < coins) return false;
        this.coins -= coins;
        return true;
    }

    public synchronized void addCoins(int coins) {
        this.coins += coins;
    }

    public ItemStack[] deck() {
        return deck;
    }

    public void setDeck(ItemStack[] deck) {
        this.deck = deck;
        sync();
    }

    public void setDeckSlot(int slot, @Nullable ItemStack item) {
        this.deck[slot] = item;
        sync();
    }

    public void addToDeck(ItemStack item) {
        for (int i = 0; i < deck.length; i++) {
            if (deck[i] == null) {
                setDeckSlot(i, item);
                return;
            }
        }
        throw new ArrayIndexOutOfBoundsException("Deck is already full!");
    }

    public boolean allowItem(Turn<?> turn, Material material) {
        int count = 0;
        for (ItemStack item : deck) {
            if (item != null && item.getType() == material) count++;
        }
        return count < turn.maxAmount();
    }

    public boolean shopDone() {
        return shopDone;
    }

    public void setShopDone(boolean shopDone) {
        this.shopDone = shopDone;
    }

    public void sync() {
        PlayerInventory inv = player.player.getInventory();
        inv.clear();
        for (int i = 0; i < deck.length; i++) {
            ItemStack item = deck[i] == null ? new ItemStack(Material.BLACK_STAINED_GLASS_PANE) : deck[i];
            inv.setItem(i, item);
        }
    }
}
