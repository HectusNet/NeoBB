package net.hectus.bb.turn.counter;

import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.turn.TurnData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Predicate;

public interface CounterFilter {
    boolean doCounter(@NotNull TurnData last);
    String text(Locale l);

    default Component line(Locale l) {
        return Component.text("   | " + text(l));
    }

    // Have to use this, because @FunctionalInterface obviously wouldn't work with two methods.
    static @NotNull CounterFilter of(Predicate<TurnData> doCounter, String name) {
        return new CounterFilter() {
            @Override
            public boolean doCounter(@NotNull TurnData last) {
                return doCounter.test(last);
            }

            @Override
            public String text(Locale l) {
                return Translation.string(l, "item-lore.counter." + name);
            }
        };
    }
}
