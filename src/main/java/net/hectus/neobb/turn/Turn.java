package net.hectus.neobb.turn;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Turn<T> {
    protected final T data;
    protected Location location;
    protected final NeoPlayer player;

    /** Used to create a dummy turn for usage in the shop. */
    public Turn(NeoPlayer player) { this(null, null, player); }

    public Turn(T data, Location location, NeoPlayer player) {
        this.data = data;
        this.location = location;
        this.player = player;
    }

    public boolean requiresUsageGuide() { return false; }
    public boolean unusable() { return false; }
    public int maxAmount() { return 2; }
    public void apply() {}
    public ItemStack item() { return ItemStack.empty(); }

    public abstract int cost();

    public List<ItemStack> items() {
        return List.of(item());
    }

    public T data() {
        return data;
    }

    public Location location() {
        return location.clone();
    }

    public NeoPlayer player() {
        return player;
    }

    public boolean goodChoice(NeoPlayer player) {
        if (unusable() || !player.game.allows(this)) return false;

        if ((player.hasModifier(Modifiers.P_DEFAULT_ATTACKED) || player.game.turnScheduler.hasTask("freeze")) && !player.hasModifier(Modifiers.P_DEFAULT_DEFENDED))
            return this instanceof CounterFunction counter && counter.counters().stream().anyMatch(filter -> filter.doCounter(player.game.history().getLast()));

        return !(this instanceof AttackFunction) || !player.nextPlayer().hasModifier(Modifiers.P_DEFAULT_DEFENDED);
    }

    public final boolean isDummy() {
        return this == DUMMY || data == null;
    }

    public static final Turn<Void> DUMMY = new Turn<>(null) {
        @Override public int cost() { return 10; }
        @Override public boolean goodChoice(NeoPlayer player) { return false; }
    };
}
