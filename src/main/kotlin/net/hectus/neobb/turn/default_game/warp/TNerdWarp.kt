package net.hectus.neobb.turn.default_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.*
import kotlin.reflect.KClass

class TNerdWarp(data: PlacedStructure?, player: NeoPlayer?) : WarpTurn(data, "nerd", player) {
    override val cost: Int = 4
    override val chance: Double = 20.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, NatureClazz::class, RedstoneClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}