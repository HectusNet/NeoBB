package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

class TLava(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction, HotClazz {
    override val cost: Int = 6
    override val requiresUsageGuide: Boolean = true
    override val maxAmount: Int = 1

    override fun item(): ItemStack = ItemStack(Material.LAVA_BUCKET)

    override fun apply() {
        player!!.nextPlayer().player.fireTicks = 6000
        player.game.turnScheduler.runTaskLater(ScheduleID.BURN, 3) {
            player.game.eliminate(player.nextPlayer())
        }
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}