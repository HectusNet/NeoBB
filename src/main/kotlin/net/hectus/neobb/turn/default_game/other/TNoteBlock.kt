package net.hectus.neobb.turn.default_game.other

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterbuffFunction
import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.inventory.ItemStack

class TNoteBlock(data: NoteBlock?, cord: Cord?, player: NeoPlayer?) : OtherTurn<NoteBlock>(data, cord, player), CounterbuffFunction, RedstoneClazz {
    companion object {
        fun luck(instrument: Instrument): Int {
            return when (instrument) {
                Instrument.BELL, Instrument.CHIME -> 35
                Instrument.COW_BELL, Instrument.BANJO, Instrument.GUITAR -> 20
                Instrument.DIDGERIDOO -> 15
                Instrument.STICKS -> 10
                Instrument.BASS_DRUM, Instrument.BASS_GUITAR -> 5
                Instrument.PIANO -> -5
                else -> 0
            }
        }
    }

    override val cost: Int = 3

    override fun item(): ItemStack = ItemStack(Material.NOTE_BLOCK)

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("solid") { it.data is Block && it.data.isSolid })
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(10))
    }

    override fun apply() {
        Luck(luck(data?.instrument ?: Instrument.CUSTOM_HEAD)).apply(player!!)
    }
}
