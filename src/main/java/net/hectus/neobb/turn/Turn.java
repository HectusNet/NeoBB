package net.hectus.neobb.turn;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class Turn<T> {
    protected final T data;
    protected final Location location;
    protected final NeoPlayer player;

    /** Used to create a dummy turn for usage in the shop. */
    public Turn(NeoPlayer player) { this(null, null, player); }

    public Turn(T data, Location location, NeoPlayer player) {
        this.data = data;
        this.location = location;
        this.player = player;
    }

    public boolean requiresUsageGuide() { return false; }
    public boolean canBeUsed() { return true; }
    public int maxAmount() { return 2; }
    public void apply() {}

    public abstract ItemStack item();
    public abstract int cost();

    public T data() {
        return data;
    }

    public Location location() {
        if (data instanceof Block) {
            return location.clone().add(0.5, 0.5, 0.5);
        } else {
            return location.clone();
        }
    }

    public NeoPlayer player() {
        return player;
    }

    public boolean goodChoice(NeoPlayer player) {
        if (!canBeUsed() || !player.game.allows(this)) return false;

        if (player.hasModifier("attacked") && !player.hasModifier("defended"))
            return this instanceof CounterFunction counter && counter.counters().stream().anyMatch(filter -> filter.doCounter(player.game.history().getLast()));

        return !(this instanceof AttackFunction) || !player.nextPlayer().hasModifier("defended");
    }

    public static final Turn<Void> DUMMY = new Turn<>(null) {
        @Override public @NotNull ItemStack item() { return ItemStack.empty(); }
        @Override public int cost() { return 0; }
        @Override public boolean goodChoice(NeoPlayer player) { return false; }
    };
}
