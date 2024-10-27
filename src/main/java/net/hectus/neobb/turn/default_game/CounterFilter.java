package net.hectus.neobb.turn.default_game;

import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Predicate;

public interface CounterFilter {
    boolean doCounter(Turn<?> last);
    String text(Locale l);

    default Component line(Locale l) {
        return Component.text("   | " + text(l)).decoration(TextDecoration.ITALIC, false);
    }

    static @NotNull CounterFilter of(Predicate<Turn<?>> doCounter, String name) {
        return new CounterFilter() {
            @Override
            public boolean doCounter(@NotNull Turn<?> last) {
                return doCounter.test(last);
            }

            @Override
            public String text(Locale l) {
                return Translation.string(l, "item-lore.counter." + name);
            }
        };
    }

    static @NotNull CounterFilter clazz(Class<? extends Clazz> turn) {
        return new CounterFilter() {
            @Override
            public boolean doCounter(@NotNull Turn<?> last) {
                return turn.isInstance(last);
            }

            @Override
            public String text(Locale l) {
                return Translation.string(l, "info.class." + Utilities.camelToSnake(Utilities.counterFilterName(turn.getSimpleName())));
            }
        };
    }
}
