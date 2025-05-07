package net.hectus.neobb.turn.default_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz
import kotlin.reflect.KClass

class TSunWarp(data: PlacedStructure?, player: NeoPlayer?) : WarpTurn(data, "sun", player) {
    override val cost: Int = 4
    override val chance: Double = 40.0
    override val allows: List<KClass<out Clazz>> = listOf(HotClazz::class, RedstoneClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.HOT
}