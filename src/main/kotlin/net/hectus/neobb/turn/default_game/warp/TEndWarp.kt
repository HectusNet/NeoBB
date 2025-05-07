package net.hectus.neobb.turn.default_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz
import kotlin.reflect.KClass

class TEndWarp(data: PlacedStructure?, player: NeoPlayer?) : WarpTurn(data, "end", player) {
    override val cost: Int = 4
    override val chance: Double = 60.0
    override val allows: List<KClass<out Clazz>> = listOf(ColdClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.COLD
}