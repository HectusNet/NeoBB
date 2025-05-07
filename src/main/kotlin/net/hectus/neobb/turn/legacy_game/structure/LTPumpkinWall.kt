package net.hectus.neobb.turn.legacy_game.structure

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.structure.StructureManager
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.turn.default_game.structure.StructureTurn

class LTPumpkinWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), BuffFunction, NatureClazz {
    override val cost: Int = 5
    override val referenceStructure: Structure = StructureManager["legacy-pumpkin_wall"]!!

    override fun apply() {
        player!!.game.time = MinecraftTime.MIDNIGHT
    }

    override fun buffs(): List<Buff<*>> = listOf(Luck(10))
}
