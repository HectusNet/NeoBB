package net.hectus.neobb.turn.person_game.categorization;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public interface DefensiveCategory extends Category {
    @Override
    default TextColor categoryColor() {
        return NamedTextColor.GREEN;
    }

    @Override
    default Material categoryItem() {
        return Material.RED_STAINED_GLASS;
    }

    @Override
    default String categoryName() {
        return "defensive";
    }

    // ========== CUSTOM CATEGORY LOGIC ==========

    void applyDefense();
}
