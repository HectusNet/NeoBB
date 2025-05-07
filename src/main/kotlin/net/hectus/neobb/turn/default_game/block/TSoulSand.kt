package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.turn.default_game.attribute.function.AttackFunction
import org.bukkit.block.Block

class TSoulSand(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction, SupernaturalClazz {
    override val cost: Int = 3
}