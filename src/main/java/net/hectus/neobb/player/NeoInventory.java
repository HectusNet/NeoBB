package net.hectus.neobb.player;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.util.ItemBuilder;
import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.person_game.categorization.Category;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

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

    public void clear() {
        for (int i = 0; i < player.game.info().deckSize(); i++) {
            this.deck[i] = null;
            this.dummyTurnDeck[i] = null;
        }
        sync();
    }

    public boolean isEmpty() {
        for (ItemStack i : deck) {
            if (i != null) return false;
        }
        return true;
    }

    public boolean isFull() {
        for (ItemStack i : deck) {
            if (i == null) return false;
        }
        return true;
    }

    public ItemStack[] deck() {
        return deck;
    }

    public Turn<?>[] dummyTurnDeck() {
        return dummyTurnDeck;
    }

    public void setDeckSlot(int slot, @Nullable ItemStack item, @Nullable Turn<?> turn) {
        this.deck[slot] = item;
        this.dummyTurnDeck[slot] = turn;
        sync();
    }

    public void addToDeck(@NotNull Turn<?> turn) {
        turn.items().forEach(item -> addToDeck(new ItemBuilder(item)
                .lore(player.shop.loreBuilder.turn(turn).buildWithTooltips(player.locale()))
                .build(), turn));
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

    public void addRandom() {
        if (isFull()) return;
        try {
            addToDeck(Randomizer.fromCollection(player.shop.dummyTurns));
        } catch (ArrayIndexOutOfBoundsException ignored) {} // Skill issue I guess.
    }

    public void removeRandom() {
        if (isEmpty()) return;
        setDeckSlot(Randomizer.fromCollection(IntStream.range(0, deck.length)
                .filter(i -> deck[i] != null)
                .boxed()
                .toList()
        ), null, null);
    }

    public void fillInRandomly() {
        for (ItemStack item : deck) {
            if (item == null) addRandom();
        }
    }

    public boolean allowItem(Turn<?> turn, Material material) {
        int count = 0;
        for (ItemStack item : deck) {
            if (item != null && item.getType() == material) count++;
        }
        return count < (turn instanceof Category personTurn ? personTurn.categoryMaxPerDeck() :  turn.maxAmount());
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

            if (player.game.difficulty.suggestions && dummyTurn.goodChoice(player))
                item.addUnsafeEnchantment(Enchantment.MENDING, 1);

            inv.setItem(i, item);
        }
        inv.setItem(13, new ItemBuilder(coins == 0 ? Material.REDSTONE : Material.GOLD_INGOT)
                .amount(Math.clamp(1, coins, 64))
                .name(Translation.component(player.locale(), "item-lore.cost.value", coins))
                .build());
    }
}
