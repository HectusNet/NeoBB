package net.hectus.neobb.turn.default_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import kotlin.reflect.KClass

class TDesertWarp(data: PlacedStructure?, player: NeoPlayer?) : WarpTurn(data, "desert", player) {
    override val cost: Int = 4
    override val chance: Double = 80.0
    override val allows: List<KClass<out Clazz>> = listOf(HotClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.HOT
}