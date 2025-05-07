package net.hectus.neobb.turn.legacy_game.structure

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.structure.StructureManager
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.turn.default_game.structure.StructureTurn

class LTDaylightSensorStrip(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), BuffFunction, CounterFunction, RedstoneClazz {
    override val cost: Int = 6
    override val referenceStructure: Structure = StructureManager["legacy-daylight_sensor_strip"]!!

    override fun apply() {
        player!!.game.time = MinecraftTime.DAY
    }

    override fun buffs(): List<Buff<*>> = emptyList()

    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(HotClazz::class),
            CounterFilter.clazz(NatureClazz::class),
            CounterFilter.clazz(WaterClazz::class),
            CounterFilter.clazz(RedstoneClazz::class)
        )
    }
}
