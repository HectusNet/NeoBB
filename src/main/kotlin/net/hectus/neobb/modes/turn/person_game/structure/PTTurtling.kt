package net.hectus.neobb.modes.turn.person_game.structure

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.structure.StructureTurn
import net.hectus.neobb.modes.turn.person_game.block.PTLever
import net.hectus.neobb.modes.turn.person_game.categorization.ArmorCategory
import net.hectus.neobb.player.NeoPlayer
import kotlin.reflect.KClass

class PTTurtling(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), ArmorCategory {
    override val staticStructure: StaticStructure = StaticStructures.Person.TURTLING

    override fun armor(): Int = 3

    override fun counteredBy(): List<KClass<out Turn<*>>> = listOf(PTLever::class)
}
