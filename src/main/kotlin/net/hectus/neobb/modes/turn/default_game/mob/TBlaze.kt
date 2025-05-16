package net.hectus.neobb.modes.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.AttackFunction
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.entity.Blaze

class TBlaze(data: Blaze?, cord: Cord?, player: NeoPlayer?) : MobTurn<Blaze>(data, cord, player), AttackFunction, HotClazz {
    override val cost: Int = 5

    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.NO_WARP.name + "_cold")
        player.game.addModifier(Modifiers.Game.NO_WARP.name + "_water")
    }
}
