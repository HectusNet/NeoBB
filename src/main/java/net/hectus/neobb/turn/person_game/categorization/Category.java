package net.hectus.neobb.turn.person_game.categorization;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public interface Category {
    TextColor categoryColor();
    default int categoryMaxPerDeck() { return 1; }
    Material categoryItem();
    String categoryName();
}
