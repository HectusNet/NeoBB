package net.hectus.neobb.modes.turn.person_game.categorization

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material

interface UtilityCategory : Category {
    override val categoryColor: TextColor get() = NamedTextColor.DARK_PURPLE
    override val categoryMaxPerDeck: Int get() = 9

    override val categoryItem: Material get() = Material.GLOWSTONE
    override val categoryName: String get() = "utility"
}
