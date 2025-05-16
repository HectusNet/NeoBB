package net.hectus.neobb.modes.turn.legacy_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.AttackFunction
import net.hectus.neobb.modes.turn.default_game.block.BlockTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.block.Block

class LTTnt(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction, RedstoneClazz {
    override val cost: Int = 2

    override fun apply() {
        val eliminations = player!!.game.players.filter { !it.hasModifier(Modifiers.Player.Default.DEFENDED) }
        if (eliminations.size >= player.game.players.size) {
            player.game.draw()
        } else {
            eliminations.forEach { player.game.eliminate(it) }
        }
    }
}
