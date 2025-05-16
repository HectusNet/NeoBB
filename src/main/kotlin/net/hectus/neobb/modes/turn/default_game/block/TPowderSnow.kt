package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

class TPowderSnow(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction, ColdClazz {
    override val cost: Int = 5
    override val maxAmount: Int = 1

    override fun item(): ItemStack = ItemStack(Material.POWDER_SNOW_BUCKET)

    override fun apply() {
        player!!.nextPlayer().player.freezeTicks = 6000
        player.game.turnScheduler.runTaskLater(ScheduleID.FREEZE, 3) {
            player.game.eliminate(player.nextPlayer())
        }
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}
