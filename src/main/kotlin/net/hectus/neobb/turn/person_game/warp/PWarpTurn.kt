package net.hectus.neobb.turn.person_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.warp.WarpTurn
import net.hectus.neobb.turn.person_game.categorization.WarpCategory
import kotlin.reflect.KClass

abstract class PWarpTurn(data: PlacedStructure?, name: String, player: NeoPlayer?) : WarpTurn(data, "legacy-$name", player), WarpCategory {
    override val cost: Int = 0
    override val allows: List<KClass<out Clazz>> = listOf(Clazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}