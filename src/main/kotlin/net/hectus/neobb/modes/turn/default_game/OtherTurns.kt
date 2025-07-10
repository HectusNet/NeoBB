package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.bukkitRunTimer
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.entity.Boat
import org.bukkit.inventory.ItemStack

abstract class OtherTurn<T>(data: T?, cord: Cord?, player: NeoPlayer?) : Turn<T>(data, cord, player)

class TBoat(data: Boat?, cord: Cord?, player: NeoPlayer?) : OtherTurn<Boat>(data, cord, player), EventFunction, WaterClazz {
    override val cost: Int = 3
    override fun item(): ItemStack = ItemStack(Material.OAK_BOAT)
    override fun triggerEvent() {
        player!!.nextPlayer().addModifier(Modifiers.Player.Default.BOAT_DAMAGE)
        bukkitRunTimer(0, 20) { r ->
            if (player.nextPlayer().hasModifier(Modifiers.Player.Default.BOAT_DAMAGE)) {
                player.nextPlayer().damage(1.0)
            } else {
                r.cancel()
            }
        }
    }
}

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
        Luck(luck(data?.instrument ?: Instrument.CUSTOM_HEAD)).invoke(player!!)
    }
}
