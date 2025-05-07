package net.hectus.neobb.turn.default_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import kotlin.reflect.KClass

class TCliffWarp(data: PlacedStructure?, player: NeoPlayer?) : WarpTurn(data, "cliff", player) {
    override val cost: Int = 4
    override val chance: Double = 60.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}