package net.hectus.neobb.turn.person_game.structure

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.structure.StructureManager
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.hectus.neobb.turn.person_game.block.PTLever
import net.hectus.neobb.turn.person_game.categorization.ArmorCategory
import kotlin.reflect.KClass

class PTTurtling(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), ArmorCategory {
    override val referenceStructure: Structure = StructureManager["oak_door_turtling"]!!

    override fun armor(): Int = 3

    override fun counteredBy(): List<KClass<out Turn<*>>> = listOf(PTLever::class)
}