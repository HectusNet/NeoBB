package net.hectus.neobb.modes.turn.person_game

import com.marcpg.libpg.storing.WeightedList
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.ItemTurn
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.inventory.ItemStack

object PTSuspiciousStew : ItemTurn("suspicious_stew"), BuffCategory {
    override val mode: String = "person"

    val BUFFS: WeightedList<(NeoPlayer) -> Unit> = WeightedList()
    init {
        BUFFS.add({ it.inventory.removeRandom() }, 10.0)
        BUFFS.add({ it.opponents().forEach { o -> o.inventory.removeRandom() } }, 10.0)
        BUFFS.add({ it.game.eliminate(it) }, 20.0)
        BUFFS.add({ it.heal(2.0) }, 10.0)
        BUFFS.add({ ExtraTurn(2).invoke(it) }, 10.0)
        BUFFS.add({ it.databaseInfo.setElo(it.databaseInfo.elo + 2) }, 10.0)
        BUFFS.add({ it.databaseInfo.setElo(it.databaseInfo.elo - 2) }, 10.0)
        BUFFS.add({ it.luck += 15 }, 10.0)
        BUFFS.add({ it.luck -= 15 }, 9.0)
        BUFFS.add({ it.game.win(it) }, 1.0)
    }

    override fun apply(exec: TurnExec<ItemStack>) {
        BUFFS.random().invoke(exec.player)
    }
}
