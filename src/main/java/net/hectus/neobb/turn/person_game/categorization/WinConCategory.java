package net.hectus.neobb.turn.person_game.categorization;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public interface WinConCategory extends AttackCategory {
    @Override
    default TextColor categoryColor() {
        return NamedTextColor.DARK_RED;
    }

    @Override
    default Material categoryItem() {
        return Material.PURPLE_WOOL;
    }

    @Override
    default String categoryName() {
        return "win_con";
    }
}
