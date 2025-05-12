package net.hectus.neobb.turn.person_game.structure

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.StaticStructure
import net.hectus.neobb.structure.StaticStructures
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.hectus.neobb.turn.person_game.categorization.UtilityCategory

class PTPumpkinWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), UtilityCategory {
    override val staticStructure: StaticStructure = StaticStructures.Person.PUMPKIN_WALL

    override fun apply() {
        player!!.game.time = MinecraftTime.MIDNIGHT
    }
}
