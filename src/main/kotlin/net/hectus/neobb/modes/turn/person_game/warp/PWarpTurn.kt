package net.hectus.neobb.modes.turn.person_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.modes.turn.default_game.warp.WarpTurn
import net.hectus.neobb.modes.turn.person_game.categorization.WarpCategory
import net.hectus.neobb.player.NeoPlayer
import kotlin.reflect.KClass

abstract class PWarpTurn(data: PlacedStructure?, cord: Cord, name: String, player: NeoPlayer?) : WarpTurn(data, cord, "person-$name", player), WarpCategory {
    override val cost: Int = 0
    override val allows: List<KClass<out Clazz>> = listOf(Clazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}
