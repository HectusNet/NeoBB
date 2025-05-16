package net.hectus.neobb.modes.turn.default_game.attribute.function

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.game.mode.LegacyGame
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.component

interface CounterFunction : Function {
    fun counters(): List<CounterFilter>

    fun counterLogic(turn: Turn<*>): Boolean {
        if (turn.player!!.game.history.isEmpty()) return true
        val last: Turn<*> = turn.player.game.history.last()

        if (turn.player.game !is LegacyGame || last.cord?.add(Cord(0.0, 1.0, 0.0)) == turn.cord) { // Only check if it's a legacy game.
            if (!turn.player.game.difficulty.completeRules || counters().any { it.doCounter(last) }) {
                counter(turn.player, last)
            } else {
                turn.player.sendMessage(turn.player.locale().component("gameplay.info.wrong_counter", color = Colors.NEGATIVE))
                turn.player.playSound(org.bukkit.Sound.ENTITY_VILLAGER_NO)
                return true
            }
        }
        return false
    }

    fun counter(source: NeoPlayer, countered: Turn<*>) {
        source.removeModifier(Modifiers.Player.Default.ATTACKED)
    }
}
