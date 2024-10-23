package net.hectus.neobb.turn.person_game.categorization;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public interface GameEndingCounterCategory extends Category {
    @Override
    default TextColor categoryColor() {
        return NamedTextColor.GOLD;
    }

    @Override
    default Material categoryItem() {
        return Material.GRAY_CARPET;
    }

    @Override
    default String categoryName() {
        return "game_ending_counter";
    }
}
