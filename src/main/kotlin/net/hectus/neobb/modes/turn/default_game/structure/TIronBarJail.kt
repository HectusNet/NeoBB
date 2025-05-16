package net.hectus.neobb.modes.turn.default_game.structure

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers

class TIronBarJail(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), BuffFunction, NeutralClazz {
    override val cost: Int = 7
    override val staticStructure: StaticStructure = StaticStructures.Default.IRON_BAR_JAIL

    private val trapped: NeoPlayer?
        get() = player?.nextPlayer()

    override fun apply() {
        // This is not checking if the player is INSIDE it, but this is easier and works well enough.
        if (trapped == null || trapped!!.location().distance(data!!.lastBlock.location) > 1.5) return

        trapped!!.addModifier(Modifiers.Player.NO_MOVE)
        trapped!!.addModifier(Modifiers.Player.Default.NO_ATTACK)

        player!!.game.turnScheduler.runTaskTimer(ScheduleID.IRON_BAR_JAIL, 1, {
            !trapped!!.hasModifier(Modifiers.Player.NO_MOVE) || !trapped!!.hasModifier(Modifiers.Player.Default.NO_ATTACK)
        }) {
            if (player.game.history.last() is CounterFunction &&
                CounterFilter.clazz(RedstoneClazz::class).doCounter(player.game.history.last()) ||
                CounterFilter.clazz(SupernaturalClazz::class).doCounter(player.game.history.last())
            ) {
                trapped!!.removeModifier(Modifiers.Player.NO_MOVE)
                trapped!!.removeModifier(Modifiers.Player.Default.NO_ATTACK)
            }
        }
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}
