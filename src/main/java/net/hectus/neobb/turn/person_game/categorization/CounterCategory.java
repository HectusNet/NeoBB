package net.hectus.neobb.turn.person_game.categorization;

import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public interface CounterCategory extends Category {
    @Override
    default TextColor categoryColor() {
        return NamedTextColor.YELLOW;
    }

    @Override
    default int categoryMaxPerDeck() {
        return 3;
    }

    @Override
    default Material categoryItem() {
        return Material.NOTE_BLOCK;
    }

    @Override
    default String categoryName() {
        return "counter";
    }

    // ========== CUSTOM CATEGORY LOGIC ==========

    default boolean counterLogic(@NotNull Turn<?> turn) {
        if (turn.player().game.history().isEmpty()) return true;
        Turn<?> last = turn.player().game.history().getLast();

        if (last instanceof AttackCategory attack && (!turn.player().game.difficulty.usageRules || properlyPlaced(turn.location(), last.location())) && attack.counteredBy().contains(turn.getClass())) {
            counter(turn.player(), last);
        } else {
            turn.player().showTitle(Title.title(Translation.component(turn.player().locale(), "gameplay.info.misplace").color(Colors.NEGATIVE), Component.empty()));
            return true;
        }
        return false;
    }

    default void counter(@NotNull NeoPlayer source, @NotNull Turn<?> countered) {
        source.heal(countered.damage());
    }

    default boolean properlyPlaced(@NotNull Location counter, @NotNull Location attack) {
        return attack.add(0, 1, 0).equals(counter);
    }
}
