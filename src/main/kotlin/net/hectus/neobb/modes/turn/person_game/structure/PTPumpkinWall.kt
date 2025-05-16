package net.hectus.neobb.modes.turn.person_game.structure

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.structure.StructureTurn
import net.hectus.neobb.modes.turn.person_game.categorization.UtilityCategory
import net.hectus.neobb.player.NeoPlayer

class PTPumpkinWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), UtilityCategory {
    override val staticStructure: StaticStructure = StaticStructures.Person.PUMPKIN_WALL

    override fun apply() {
        player!!.game.time = MinecraftTime.MIDNIGHT
    }
}
