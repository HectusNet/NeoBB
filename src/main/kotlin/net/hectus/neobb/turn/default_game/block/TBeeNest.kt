package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.function.AttackFunction
import net.hectus.neobb.turn.default_game.mob.TBee
import net.hectus.neobb.util.asCord
import org.bukkit.block.Block
import org.bukkit.entity.Bee

class TBeeNest(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction, NatureClazz {
    override val cost: Int = 4

    override fun apply() {
        if (Randomizer.boolByChance(20.0)) {
            val bee = TBee(player!!.game.world.spawn(location(), Bee::class.java), location().asCord(), player)
            bee.buffs().forEach { it.apply(player) }
            bee.apply()
        }
    }
}
