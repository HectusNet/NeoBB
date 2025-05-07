package net.hectus.neobb.turn.default_game.structure

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.structure.StructureManager
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.function.EventFunction
import net.hectus.neobb.util.Modifiers

class TRedstoneWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), EventFunction, RedstoneClazz {
    override val cost: Int = 4
    override val referenceStructure: Structure = StructureManager["redstone_wall"]!!

    override fun triggerEvent() {
        player!!.game.addModifier(Modifiers.Game.REDSTONE_POWER) // TODO: Power all blocks if this modifier is present.
        player.game.turnScheduler.runTaskLater(ScheduleID.REDSTONE_POWER, 3) {
            player.game.removeModifier(Modifiers.Game.REDSTONE_POWER)
        }
    }
}
