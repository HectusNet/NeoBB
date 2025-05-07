package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import kotlin.reflect.KClass

class LTNetherWarp(data: PlacedStructure?, player: NeoPlayer?) : LWarpTurn(data, "nether", player) {
    override val cost: Int = 4
    override val chance: Double = 80.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, HotClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.HOT
}