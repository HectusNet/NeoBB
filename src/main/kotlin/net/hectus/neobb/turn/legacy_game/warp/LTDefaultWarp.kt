package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.*
import kotlin.reflect.KClass

class LTDefaultWarp(data: PlacedStructure?, player: NeoPlayer?) : LWarpTurn(data, "default", player) {
    override val chance: Double = 0.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, WaterClazz::class, NatureClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}