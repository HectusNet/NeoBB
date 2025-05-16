package net.hectus.neobb.modes.turn.legacy_game.other

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterattackFunction
import net.hectus.neobb.modes.turn.default_game.block.TGoldBlock
import net.hectus.neobb.modes.turn.default_game.other.OtherTurn
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.inventory.ItemStack

class LTNoteBlock(data: NoteBlock?, cord: Cord?, player: NeoPlayer?) : OtherTurn<NoteBlock>(data, cord, player), CounterFunction, BuffFunction, RedstoneClazz {
    override val cost: Int = 3

    override fun item(): ItemStack = ItemStack(Material.NOTE_BLOCK)

    override fun apply() {
        Luck(luck(data!!.instrument)).apply(player!!)
    }

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("solid") { it.data is Block && it.data.isSolid && (it !is CounterattackFunction || it is TGoldBlock) })
    }

    override fun buffs(): List<Buff<*>> = emptyList()

    fun luck(instrument: Instrument): Int {
        return when (instrument) {
            Instrument.BELL -> 35
            Instrument.CHIME -> 30
            Instrument.GUITAR, Instrument.BANJO, Instrument.COW_BELL -> 20
            Instrument.DIDGERIDOO -> 15
            Instrument.STICKS -> 10
            Instrument.BASS_GUITAR -> 5
            Instrument.BASS_DRUM -> -5
            else -> -20
        }
    }
}
