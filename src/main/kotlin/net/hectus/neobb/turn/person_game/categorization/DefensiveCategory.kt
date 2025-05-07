package net.hectus.neobb.turn.person_game.categorization

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material

interface DefensiveCategory : Category {
    override val categoryColor: TextColor get() = NamedTextColor.GREEN

    override val categoryItem: Material get() = Material.RED_STAINED_GLASS
    override val categoryName: String get() = "defensive"

    // ========== CUSTOM CATEGORY LOGIC ==========
    fun applyDefense()
}
