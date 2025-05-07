package net.hectus.neobb.turn.person_game.categorization

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material

interface GameEndingCounterCategory : Category {
    override val categoryColor: TextColor get() = NamedTextColor.GOLD

    override val categoryItem: Material get() = Material.GRAY_CARPET
    override val categoryName: String get() = "game_ending_counter"
}
