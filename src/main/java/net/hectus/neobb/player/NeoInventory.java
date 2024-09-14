package net.hectus.neobb.player;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.shop.Shop;
import net.hectus.neobb.turn.Turn;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NeoInventory {
    private NeoPlayer player;
    private final ItemStack[] deck;
    private final Turn<?>[] dummyTurnDeck;
    private int coins;
    private boolean shopDone;

    public NeoInventory(@NotNull NeoPlayer player) {
        this.player = player;
        this.coins = player.game.info().coins();
        this.deck = new ItemStack[player.game.info().deckSize()];
        this.dummyTurnDeck = new Turn<?>[player.game.info().deckSize()];
        sync();
    }

    public void switchOwner(NeoPlayer player) {
        this.player = player;
    }

    public boolean isEmpty() {
        for (ItemStack i : deck) {
            if (i != null) return false;
        }
        return true;
    }

    public ItemStack[] deck() {
        return deck;
    }

    public void setDeckSlot(int slot, @Nullable ItemStack item, Turn<?> turn) {
        this.deck[slot] = item;
        this.dummyTurnDeck[slot] = turn;
        sync();
    }

    public void addToDeck(ItemStack item, Turn<?> turn) {
        for (int i = 0; i < deck.length; i++) {
            if (deck[i] == null) {
                setDeckSlot(i, item, turn);
                return;
            }
        }
        throw new ArrayIndexOutOfBoundsException("Deck is already full!");
    }

    public void fillInRandomly() {
        for (int i = 0; i < deck.length; i++) {
            if (deck[i] == null) {
                Turn<?> turn = Shop.turn(Randomizer.fromCollection(player.game.shop().turns), player);
                setDeckSlot(i, turn.items().getFirst(), turn);
            }
        }
    }

    public boolean allowItem(Turn<?> turn, Material material) {
        int count = 0;
        for (ItemStack item : deck) {
            if (item != null && item.getType() == material) count++;
        }
        return count < turn.maxAmount();
    }

    public synchronized boolean removeCoins(int coins) {
        if (this.coins < coins) return false;
        this.coins -= coins;
        player.inventory.sync();
        return true;
    }

    public synchronized void addCoins(int coins) {
        this.coins += coins;
        player.inventory.sync();
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
            ItemStack item = deck[i] == null ? new ItemStack(Material.BLACK_STAINED_GLASS_PANE) : deck[i].clone();
            Turn<?> dummyTurn = dummyTurnDeck[i] == null ? Turn.DUMMY : dummyTurnDeck[i];

            if (dummyTurn.goodChoice(player))
                item.addUnsafeEnchantment(Enchantment.MENDING, 1);

            inv.setItem(i, item);
        }
        inv.setItem(13, new ItemStack(Material.GOLD_INGOT, Math.min(coins, 64)));
    }
}
