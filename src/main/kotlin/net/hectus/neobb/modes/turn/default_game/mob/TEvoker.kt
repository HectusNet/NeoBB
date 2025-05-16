package net.hectus.neobb.modes.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterbuffFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.DyeColor
import org.bukkit.entity.Evoker

class TEvoker(data: Evoker?, cord: Cord?, player: NeoPlayer?) : MobTurn<Evoker>(data, cord, player), CounterbuffFunction, SupernaturalClazz {
    override val cost: Int = 4

    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(20), ExtraTurn())
    }

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("blue-sheep") { turn -> turn is TSheep && turn.data!!.color == DyeColor.BLUE })
    }
}
