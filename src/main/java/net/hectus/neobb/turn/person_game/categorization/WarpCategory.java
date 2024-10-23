package net.hectus.neobb.turn.person_game.categorization;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public interface WarpCategory extends Category {
    @Override
    default TextColor categoryColor() {
        return NamedTextColor.BLUE;
    }

    @Override
    default Material categoryItem() {
        return Material.END_ROD;
    }

    @Override
    default String categoryName() {
        return "warp";
    }
}
