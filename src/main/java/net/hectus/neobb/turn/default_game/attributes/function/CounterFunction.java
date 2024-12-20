package net.hectus.neobb.turn.default_game.attributes.function;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.storing.Cord;
import net.hectus.neobb.game.mode.LegacyGame;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CounterFunction extends Function {
    List<CounterFilter> counters();

    default boolean counterLogic(@NotNull Turn<?> turn) {
        if (turn.player().game.history().isEmpty()) return true;
        Turn<?> last = turn.player().game.history().getLast();

        if (!(turn.player().game instanceof LegacyGame) || last.cord().add(new Cord(0, 1, 0)).equals(turn.cord())) { // Only check if it's a legacy game.
            if (counters().stream().anyMatch(filter -> filter.doCounter(last))) {
                counter(turn.player(), last);
            } else {
                turn.player().sendMessage(Translation.component(turn.player().locale(), "gameplay.info.wrong_counter").color(Colors.NEGATIVE));
                turn.player().playSound(Sound.ENTITY_VILLAGER_NO, 1.0f);
                return true;
            }
        }
        return false;
    }

    default void counter(@NotNull NeoPlayer source, @NotNull Turn<?> countered) {
        source.removeModifier(Modifiers.P_DEFAULT_ATTACKED);
    }
}
