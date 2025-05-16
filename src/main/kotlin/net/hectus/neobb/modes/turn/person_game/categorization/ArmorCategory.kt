package net.hectus.neobb.modes.turn.person_game.categorization

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material

interface ArmorCategory : AttackCategory {
    override val categoryColor: TextColor get() = NamedTextColor.AQUA

    override val categoryItem: Material get() = Material.OAK_DOOR
    override val categoryName: String get() = "armor"

    fun armor(): Int
}
