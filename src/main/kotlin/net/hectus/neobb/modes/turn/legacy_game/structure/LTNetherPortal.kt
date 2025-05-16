package net.hectus.neobb.modes.turn.legacy_game.structure

import com.marcpg.libpg.data.time.Time
import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.game.Game
import net.hectus.neobb.modes.turn.ComboTurn
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.AttackFunction
import net.hectus.neobb.modes.turn.default_game.other.OtherTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.bukkitRunLater
import org.bukkit.block.BlockState

class LTNetherPortal(data: List<BlockState>?, cord: Cord?, player: NeoPlayer?) : OtherTurn<List<BlockState>>(data, cord, player), ComboTurn, AttackFunction, HotClazz {
    companion object {
        fun await(game: Game) {
            game.addModifier(Modifiers.Game.Legacy.PORTAL_AWAIT)
            bukkitRunLater(Time(5)) {
                if (game.hasModifier(Modifiers.Game.Legacy.PORTAL_AWAIT)) {
                    game.removeModifier(Modifiers.Game.Legacy.PORTAL_AWAIT)
                    game.draw()
                }
            }
        }
    }

    override val cost: Int = 3
}
