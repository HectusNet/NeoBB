package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.util.bukkitRunTimer
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.attribute.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.CounterbuffFunction
import net.hectus.neobb.modes.turn.default_game.attribute.EventFunction
import net.hectus.neobb.modes.turn.default_game.attribute.TurnClazz
import net.hectus.neobb.util.Modifiers
import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.entity.Boat
import org.bukkit.inventory.ItemStack

abstract class OtherTurn<T>(namespace: String) : Turn<T>(namespace)

object TBoat : OtherTurn<Boat>("boat"), EventFunction {
    override val mode: String = "default"
    override val event: TurnEvent = TurnEvent.CUSTOM
    override val clazz: TurnClazz? = TurnClazz.WATER
    override val cost: Int = 3

    override val mainItem: ItemStack = ItemStack.of(Material.OAK_BOAT)

    override fun triggerEvent(exec: TurnExec<*>) {
        exec.player.targetPlayer().addModifier(Modifiers.Player.Default.BOAT_DAMAGE)
        bukkitRunTimer(0, 20) { r ->
            if (exec.player.targetPlayer().hasModifier(Modifiers.Player.Default.BOAT_DAMAGE)) {
                exec.player.targetPlayer().damage(1.0)
            } else {
                r.cancel()
            }
        }
    }
}

object TNoteBlock : OtherTurn<NoteBlock>("note_block"), CounterbuffFunction {
    override val mode: String = "default"
    override val event: TurnEvent = TurnEvent.CUSTOM
    override val clazz: TurnClazz? = TurnClazz.REDSTONE
    override val cost: Int? = 3

    override val counters: List<CounterFilter> = listOf(CounterFilter.of("solid") { it.data is Block && it.data.isSolid })
    override val buffs: List<Buff<*>> = listOf(Luck(10))

    override fun apply(exec: TurnExec<NoteBlock>) {
        Luck(luck(exec.data.instrument)).invoke(exec.player)
    }

    fun luck(instrument: Instrument) = when (instrument) {
        Instrument.BELL, Instrument.CHIME -> 35
        Instrument.COW_BELL, Instrument.BANJO, Instrument.GUITAR -> 20
        Instrument.DIDGERIDOO -> 15
        Instrument.STICKS -> 10
        Instrument.BASS_DRUM, Instrument.BASS_GUITAR -> 5
        Instrument.PIANO -> -5
        else -> 0
    }
}
