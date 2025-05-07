package net.hectus.neobb.turn.default_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.*
import kotlin.reflect.KClass

class TVoidWarp(data: PlacedStructure?, player: NeoPlayer?) : WarpTurn(data, "void", player) {
    override val cost: Int = 4
    override val chance: Double = 10.0
    override val allows: List<KClass<out Clazz>> = listOf(ColdClazz::class, NeutralClazz::class, WaterClazz::class, RedstoneClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.COLD
}