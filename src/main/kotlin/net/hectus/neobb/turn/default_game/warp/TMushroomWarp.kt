package net.hectus.neobb.turn.default_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz
import kotlin.reflect.KClass

class TMushroomWarp(data: PlacedStructure?, player: NeoPlayer?) : WarpTurn(data, "mushroom", player) {
    override val cost: Int = 4
    override val chance: Double = 50.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, NatureClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}