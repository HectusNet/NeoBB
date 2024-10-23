package net.hectus.neobb.turn.person_game.categorization;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public interface DefensiveCounterCategory extends CounterCategory {
    @Override
    default TextColor categoryColor() {
        return NamedTextColor.DARK_GREEN;
    }

    @Override
    default Material categoryItem() {
        return Material.LEVER;
    }

    @Override
    default String categoryName() {
        return "defensive_counter";
    }

    @Override
    default boolean properlyPlaced(@NotNull Location counter, @NotNull Location attack) {
        return attack.distance(counter) <= 2.25; // Small buffer, just in case.
    }

    @Override
    default void counter(@NotNull NeoPlayer source, @NotNull Turn<?> countered) {
        CounterCategory.super.counter(source, countered);
        countered.player().damage(2);
    }
}
