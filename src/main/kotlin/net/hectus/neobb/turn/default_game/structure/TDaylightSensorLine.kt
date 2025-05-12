package net.hectus.neobb.turn.default_game.structure

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.StaticStructure
import net.hectus.neobb.structure.StaticStructures
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterbuffFunction

class TDaylightSensorLine(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), CounterbuffFunction, RedstoneClazz {
    override val cost: Int = 6
    override val staticStructure: StaticStructure = StaticStructures.Default.DAYLIGHT_SENSOR_LINE

    override fun apply() {
        player!!.game.time = MinecraftTime.DAY
    }

    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(HotClazz::class),
            CounterFilter.clazz(NatureClazz::class),
            CounterFilter.clazz(WaterClazz::class),
            CounterFilter.clazz(RedstoneClazz::class)
        )
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}
