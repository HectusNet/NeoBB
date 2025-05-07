package net.hectus.neobb.turn.person_game.categorization

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material

interface WinConCategory : AttackCategory {
    override val categoryColor: TextColor get() = NamedTextColor.DARK_RED

    override val categoryItem: Material get() = Material.PURPLE_WOOL
    override val categoryName: String get() = "win_con"
}
