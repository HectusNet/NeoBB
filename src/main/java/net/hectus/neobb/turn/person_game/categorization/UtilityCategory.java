package net.hectus.neobb.turn.person_game.categorization;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public interface UtilityCategory extends Category {
    @Override
    default TextColor categoryColor() {
        return NamedTextColor.DARK_PURPLE;
    }

    @Override
    default int categoryMaxPerDeck() {
        return 9;
    }

    @Override
    default Material categoryItem() {
        return Material.GLOWSTONE;
    }

    @Override
    default String categoryName() {
        return "utility";
    }
}
