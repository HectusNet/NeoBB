package net.hectus.neobb.modes.turn.person_game.categorization

import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material

interface Category {
    val categoryColor: TextColor
    val categoryMaxPerDeck: Int get() = 1

    val categoryItem: Material
    val categoryName: String
}
