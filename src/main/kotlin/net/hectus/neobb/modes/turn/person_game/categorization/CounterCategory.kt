package net.hectus.neobb.modes.turn.person_game.categorization

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.component
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import org.bukkit.Material

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
        return attack.add(Cord(0.0, 1.0, 0.0)) == counter
    }
}
