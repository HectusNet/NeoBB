package net.hectus.neobb.turn.person_game.categorization

import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.attribute.function.AttackFunction
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import kotlin.reflect.KClass

interface AttackCategory : Category, AttackFunction {
    override val categoryColor: TextColor get() = NamedTextColor.RED
    override val categoryMaxPerDeck: Int get() = 9

    override val categoryItem: Material get() = Material.BIRCH_LOG
    override val categoryName: String get() = "attacked"

    // ========== CUSTOM CATEGORY LOGIC ==========
    fun counteredBy(): List<KClass<out Turn<*>>> {
        return emptyList() // Nothing counters it by default!
    }
}
