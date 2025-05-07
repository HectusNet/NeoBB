package net.hectus.neobb.turn.person_game.item

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.storing.WeightedList
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.item.ItemTurn
import net.hectus.neobb.turn.person_game.categorization.BuffCategory
import org.bukkit.inventory.ItemStack

class PTSuspiciousStew(data: ItemStack?, cord: Cord?, player: NeoPlayer?) : ItemTurn(data, cord, player), BuffCategory {
    companion object {
        val BUFFS: WeightedList<(NeoPlayer) -> Unit> = WeightedList()

        init {
            BUFFS.add({ it.inventory.removeRandom() }, 10.0)
            BUFFS.add({ it.opponents().forEach { o -> o.inventory.removeRandom() } }, 10.0)
            BUFFS.add({ it.game.eliminate(it) }, 20.0)
            BUFFS.add({ it.heal(2.0) }, 10.0)
            BUFFS.add({ ExtraTurn(2).apply(it) }, 10.0)
            BUFFS.add({ it.databaseInfo.setElo(it.databaseInfo.elo + 2) }, 10.0)
            BUFFS.add({ it.databaseInfo.setElo(it.databaseInfo.elo - 2) }, 10.0)
            BUFFS.add({ it.addLuck(15) }, 10.0)
            BUFFS.add({ it.addLuck(-15) }, 9.0)
            BUFFS.add({ it.game.win(it) }, 1.0)
        }
    }

    override fun apply() {
        BUFFS.random().invoke(player!!)
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}
