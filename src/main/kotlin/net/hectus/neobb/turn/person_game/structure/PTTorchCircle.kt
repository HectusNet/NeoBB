package net.hectus.neobb.turn.person_game.structure

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.structure.StructureManager
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.hectus.neobb.turn.person_game.categorization.UtilityCategory
import net.hectus.neobb.turn.person_game.warp.PTVillagerWarp

class PTTorchCircle(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), UtilityCategory {
    override val referenceStructure: Structure = StructureManager["torch_circle"]!!

    override fun apply() {
        player!!.addArmor(4.0)
    }

    override fun unusable(): Boolean {
        return player!!.game.warp !is PTVillagerWarp
    }
}