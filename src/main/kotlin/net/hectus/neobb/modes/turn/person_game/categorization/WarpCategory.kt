package net.hectus.neobb.modes.turn.person_game.categorization

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material

interface WarpCategory : Category {
    override val categoryColor: TextColor get() = NamedTextColor.BLUE

    override val categoryItem: Material get() = Material.END_ROD
    override val categoryName: String get() = "warp"
}
