package net.hectus.neobb.turn.default_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import kotlin.reflect.KClass

class TFrozenWarp(data: PlacedStructure?, player: NeoPlayer?) : WarpTurn(data, "frozen", player) {
    override val cost: Int = 4
    override val chance: Double = 70.0
    override val allows: List<KClass<out Clazz>> = listOf(ColdClazz::class, WaterClazz::class)
    override val temperature: Temperature = Temperature.COLD
}