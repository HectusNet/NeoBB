package net.hectus.neobb.turn.default_game.structure

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.StaticStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.turn.Turn
import org.bukkit.inventory.ItemStack

abstract class StructureTurn(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : Turn<PlacedStructure>(data, cord, player) {
    abstract val staticStructure: StaticStructure
    val referenceStructure: Structure
        get() = staticStructure.structure

    override fun items(): List<ItemStack> = referenceStructure.items()
}
