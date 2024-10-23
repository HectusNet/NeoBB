package net.hectus.neobb.turn.person_game.categorization;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public interface ArmorCategory extends AttackCategory {
    @Override
    default TextColor categoryColor() {
        return NamedTextColor.AQUA;
    }

    @Override
    default Material categoryItem() {
        return Material.OAK_DOOR;
    }

    @Override
    default String categoryName() {
        return "armor";
    }

    int armor();
}
