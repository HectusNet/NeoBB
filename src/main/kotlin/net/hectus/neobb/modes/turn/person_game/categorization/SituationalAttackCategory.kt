package net.hectus.neobb.modes.turn.person_game.categorization

import org.bukkit.Material

interface SituationalAttackCategory : AttackCategory {
    override val categoryMaxPerDeck: Int get() = 1

    override val categoryItem: Material get() = Material.SNOWBALL
    override val categoryName: String get() = "situational_attack"
}
