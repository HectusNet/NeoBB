package net.hectus.neobb.turn.person_game.categorization;

import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public interface BuffCategory extends Category, BuffFunction {
    @Override
    default TextColor categoryColor() {
        return NamedTextColor.LIGHT_PURPLE;
    }

    @Override
    default int categoryMaxPerDeck() {
        return 9;
    }

    @Override
    default Material categoryItem() {
        return Material.CAKE;
    }

    @Override
    default String categoryName() {
        return "buff";
    }
}
