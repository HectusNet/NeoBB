package net.hectus.neobb.turn.default_game.attributes.function;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.util.Modifiers;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CounterFunction extends Function {
    List<CounterFilter> counters();

    default void counter(@NotNull NeoPlayer source) {
        source.removeModifier(Modifiers.P_DEFAULT_ATTACKED);
    }
}
