package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import org.bukkit.block.Block

class TPinkBed(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction, SupernaturalClazz {
    override val cost: Int = 4

    override fun apply() {
        if (Randomizer.boolByChance(70.0)) {
            player!!.player.clearActivePotionEffects()
        }
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}