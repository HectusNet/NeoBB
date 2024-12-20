package net.hectus.neobb.turn;

import com.marcpg.libpg.storing.Cord;
import com.marcpg.libpg.storing.CordMinecraftAdapter;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.turn.person_game.categorization.AttackCategory;
import net.hectus.neobb.turn.person_game.categorization.CounterCategory;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Turn<T> {
    protected final T data;
    protected Cord cord;
    protected final NeoPlayer player;

    /** Used to create a dummy turn for usage in the shop. */
    public Turn(NeoPlayer player) { this(null, (Cord) null, player); }

    public Turn(T data, Cord cord, NeoPlayer player) {
        this.data = data;
        this.cord = cord;
        this.player = player;
    }

    protected Turn(T data, Location location, NeoPlayer player) {
        this.data = data;
        this.cord = CordMinecraftAdapter.ofLocation(location);
        this.player = player;
    }

    public boolean requiresUsageGuide() { return false; }
    public boolean unusable() { return false; }
    public int maxAmount() { return 2; }
    public void apply() {}
    public ItemStack item() { return ItemStack.empty(); }

    public double damage() { return 0.0; }

    public int cost() { return 0; } // For games that don't have coins in their "shop".

    public List<ItemStack> items() {
        return List.of(item());
    }

    public T data() {
        return data;
    }

    public Cord cord() {
        return cord;
    }

    public Location location() {
        return CordMinecraftAdapter.toLocation(cord, player.game.world());
    }

    public NeoPlayer player() {
        return player;
    }

    public boolean goodChoice(@NotNull NeoPlayer player) {
        if (!player.game.difficulty.usageRules && (unusable() || !player.game.allows(this))) return false;

        if ((player.hasModifier(Modifiers.P_DEFAULT_ATTACKED) || player.game.turnScheduler.hasTask("freeze")) && !player.hasModifier(Modifiers.P_DEFAULT_DEFENDED)) {
            if (player.game.history().isEmpty()) return true;
            Turn<?> last = player.game.history().getLast();

            if (this instanceof CounterFunction counter) {
                return counter.counters().stream().anyMatch(filter -> filter.doCounter(last));
            } else if (this instanceof CounterCategory counter) {
                return last instanceof AttackCategory attack && attack.counteredBy().contains(counter.getClass());
            }
        }

        return !(this instanceof AttackFunction) || !player.nextPlayer().hasModifier(Modifiers.P_DEFAULT_DEFENDED);
    }

    public final boolean isDummy() {
        return this == DUMMY || data == null;
    }

    public static final Turn<Void> DUMMY = new Turn<>(null) {
        @Override public int cost() { return 10; }
        @Override public boolean goodChoice(@NotNull NeoPlayer player) { return false; }
    };
}
