package net.hectus.neobb.modes.turn.default_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.player.NeoPlayer
import kotlin.reflect.KClass

class TFrozenWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "frozen", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.FROZEN

    override val cost: Int = 4
    override val chance: Double = 70.0
    override val allows: List<KClass<out Clazz>> = listOf(ColdClazz::class, WaterClazz::class)
    override val temperature: Temperature = Temperature.COLD
}
