package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.*
import kotlin.reflect.KClass

class LTSnowWarp(data: PlacedStructure?, player: NeoPlayer?) : LWarpTurn(data, "snow", player) {
    override val cost: Int = 4
    override val chance: Double = 80.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, ColdClazz::class, WaterClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.COLD
}