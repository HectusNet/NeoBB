package net.hectus.neobb.modes.turn.person_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.component
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.attribute.AttackFunction
import net.hectus.neobb.modes.turn.default_game.attribute.BuffFunction
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import org.bukkit.Material
import kotlin.reflect.KClass

interface Category {
    val categoryColor: TextColor
    val categoryMaxPerDeck: Int get() = 1

    val categoryItem: Material
    val categoryName: String
}

interface ArmorCategory : AttackCategory {
    override val categoryColor: TextColor get() = NamedTextColor.AQUA

    override val categoryItem: Material get() = Material.OAK_DOOR
    override val categoryName: String get() = "armor"

    fun armor(): Int
}

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

interface BuffCategory : Category, BuffFunction {
    override val categoryColor: TextColor get() = NamedTextColor.LIGHT_PURPLE
    override val categoryMaxPerDeck: Int get() = 9

    override val categoryItem: Material get() = Material.CAKE
    override val categoryName: String get() = "buff"
}

interface CounterCategory : Category {
    override val categoryColor: TextColor get() = NamedTextColor.YELLOW
    override val categoryMaxPerDeck: Int get() = 3

    override val categoryItem: Material get() = Material.NOTE_BLOCK
    override val categoryName: String get() = "counter"

    // ========== CUSTOM CATEGORY LOGIC ==========
    fun counterLogic(turn: Turn<*>): Boolean {
        if (turn.player!!.game.history.isEmpty()) return true
        val last: Turn<*> = turn.player.game.history.last()

        if (last is AttackCategory && (!turn.player.game.difficulty.completeRules || properlyPlaced(turn.cord!!, last.cord!!)) && last.counteredBy().contains(turn::class)) {
            counter(turn.player, last)
        } else {
            turn.player.showTitle(Title.title(turn.player.locale().component("gameplay.info.misplace", color = Colors.NEGATIVE), Component.empty()))
            return true
        }
        return false
    }

    fun counter(source: NeoPlayer, countered: Turn<*>) {
        source.heal(countered.damage)
    }

    fun properlyPlaced(counter: Cord, attack: Cord): Boolean {
        return (attack + Cord(0.0, 1.0, 0.0)) == counter
    }
}

interface DefensiveCategory : Category {
    override val categoryColor: TextColor get() = NamedTextColor.GREEN

    override val categoryItem: Material get() = Material.RED_STAINED_GLASS
    override val categoryName: String get() = "defensive"

    // ========== CUSTOM CATEGORY LOGIC ==========
    fun applyDefense()
}

interface DefensiveCounterCategory : CounterCategory {
    override val categoryColor: TextColor get() = NamedTextColor.DARK_GREEN

    override val categoryItem: Material get() = Material.LEVER
    override val categoryName: String get() = "defensive_counter"

    override fun properlyPlaced(counter: Cord, attack: Cord): Boolean {
        return attack.distance(counter) <= 2.25 // Small buffer, just in case.
    }

    override fun counter(source: NeoPlayer, countered: Turn<*>) {
        super.counter(source, countered)
        countered.player!!.damage(2.0)
    }
}

interface GameEndingCounterCategory : Category {
    override val categoryColor: TextColor get() = NamedTextColor.GOLD

    override val categoryItem: Material get() = Material.GRAY_CARPET
    override val categoryName: String get() = "game_ending_counter"
}

interface SituationalAttackCategory : AttackCategory {
    override val categoryMaxPerDeck: Int get() = 1

    override val categoryItem: Material get() = Material.SNOWBALL
    override val categoryName: String get() = "situational_attack"
}

interface UtilityCategory : Category {
    override val categoryColor: TextColor get() = NamedTextColor.DARK_PURPLE
    override val categoryMaxPerDeck: Int get() = 9

    override val categoryItem: Material get() = Material.GLOWSTONE
    override val categoryName: String get() = "utility"
}

interface WarpCategory : Category {
    override val categoryColor: TextColor get() = NamedTextColor.BLUE

    override val categoryItem: Material get() = Material.END_ROD
    override val categoryName: String get() = "warp"
}

interface WinConCategory : AttackCategory {
    override val categoryColor: TextColor get() = NamedTextColor.DARK_RED

    override val categoryItem: Material get() = Material.PURPLE_WOOL
    override val categoryName: String get() = "win_con"
}
