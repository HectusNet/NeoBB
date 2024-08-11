package net.hectus.neobb.turn;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class Turn<T> {
    protected final T data;
    protected final NeoPlayer player;

    /** Used to create a dummy turn for usage in the shop. */
    public Turn(NeoPlayer player) { this(null, player); }

    public Turn(T data, NeoPlayer player) {
        this.data = data;
        this.player = player;
    }

    public boolean requiresUsageGuide() { return false; }
    public boolean canBeUsed() { return true; }
    public void apply() {}

    public abstract ItemStack item();
    public abstract int cost();

    public T data() {
        return data;
    }

    public NeoPlayer player() {
        return player;
    }

    public static final Turn<Void> DUMMY = new Turn<>(null, null) {
        @Override public void apply() {}
        @Override public @NotNull ItemStack item() { return ItemStack.empty(); }
        @Override public int cost() { return 0; }
    };
}
