package net.hectus.neobb.turn.default_game.structure

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.structure.StructureManager
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.turn.default_game.attribute.function.DefenseFunction
import net.hectus.neobb.util.Modifiers
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class TOakDoorTurtling(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), DefenseFunction, NeutralClazz {
    override val cost: Int = 4
    override val referenceStructure: Structure = StructureManager["oak_door_turtling"]!!

    override fun items(): List<ItemStack> {
        // Not sure if it would be OAK_DOOR or some lower part thing with the automatic item detection based on the saved structure.
        return listOf(ItemStack(Material.OAK_DOOR, 4))
    }

    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
        player.game.turnScheduler.runTaskTimer(ScheduleID.OAK_DOOR_TURTLING, 1, {
            !player.hasModifier(Modifiers.Player.Default.DEFENDED)
        }) {
            if (player.game.history.last() is CounterFunction &&
                CounterFilter.clazz(HotClazz::class).doCounter(player.game.history.last()) ||
                CounterFilter.clazz(RedstoneClazz::class).doCounter(player.game.history.last())
            ) {
                player.removeModifier(Modifiers.Player.Default.DEFENDED)
            }
        }
    }
}