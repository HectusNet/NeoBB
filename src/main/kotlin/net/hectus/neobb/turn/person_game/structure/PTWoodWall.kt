package net.hectus.neobb.turn.person_game.structure

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.structure.StructureManager
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.hectus.neobb.turn.person_game.categorization.DefensiveCategory
import net.hectus.neobb.util.Modifiers

class PTWoodWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), DefensiveCategory {
    override val referenceStructure: Structure = StructureManager["wood_wall"]!!

    override fun apply() {
        player!!.heal(1.0)
        if (Randomizer.boolByChance(15.0))
            ExtraTurn().apply(player)
    }

    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
    }
}