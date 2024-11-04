package net.hectus.neobb.turn.person_game.categorization;

import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

import java.util.List;

public interface AttackCategory extends Category, AttackFunction {
    @Override
    default TextColor categoryColor() {
        return NamedTextColor.RED;
    }

    @Override
    default int categoryMaxPerDeck() {
        return 9;
    }

    @Override
    default Material categoryItem() {
        return Material.BIRCH_LOG;
    }

    @Override
    default String categoryName() {
        return "attack";
    }

    // ========== CUSTOM CATEGORY LOGIC ==========

    default List<Class<? extends Turn<?>>> counteredBy() {
        return List.of(); // Nothing counters it by default!
    }
}
