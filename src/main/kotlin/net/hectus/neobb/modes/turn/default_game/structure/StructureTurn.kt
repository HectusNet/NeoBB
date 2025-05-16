package net.hectus.neobb.modes.turn.default_game.structure

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.Structure
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.inventory.ItemStack

abstract class StructureTurn(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : Turn<PlacedStructure>(data, cord, player) {
    abstract val staticStructure: StaticStructure
    val referenceStructure: Structure
        get() = staticStructure.structure

    override fun items(): List<ItemStack> = referenceStructure.items()
}
