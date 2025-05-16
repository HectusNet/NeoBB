package net.hectus.neobb.modes.turn.person_game.structure

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.structure.StructureTurn
import net.hectus.neobb.modes.turn.person_game.categorization.UtilityCategory
import net.hectus.neobb.modes.turn.person_game.warp.PTVillagerWarp
import net.hectus.neobb.player.NeoPlayer

class PTTorchCircle(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), UtilityCategory {
    override val staticStructure: StaticStructure = StaticStructures.Person.TORCH_CIRCLE

    override fun apply() {
        player!!.addArmor(4.0)
    }

    override fun unusable(): Boolean {
        return player!!.game.warp !is PTVillagerWarp
    }
}
