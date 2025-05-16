package net.hectus.neobb.modes.turn.person_game.categorization

import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material

interface BuffCategory : Category, BuffFunction {
    override val categoryColor: TextColor get() = NamedTextColor.LIGHT_PURPLE
    override val categoryMaxPerDeck: Int get() = 9

    override val categoryItem: Material get() = Material.CAKE
    override val categoryName: String get() = "buff"
}
