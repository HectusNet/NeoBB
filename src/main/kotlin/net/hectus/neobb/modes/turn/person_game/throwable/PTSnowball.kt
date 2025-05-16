package net.hectus.neobb.modes.turn.person_game.throwable

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.modes.turn.default_game.throwable.ThrowableTurn
import net.hectus.neobb.modes.turn.person_game.categorization.SituationalAttackCategory
import net.hectus.neobb.modes.turn.person_game.warp.PTSnowWarp
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.entity.Projectile

class PTSnowball(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), SituationalAttackCategory {
    override val damage: Double = 2.0

    override fun apply() {
        ExtraTurn().apply(player!!)
    }

    override fun unusable(): Boolean {
        return player!!.game.warp !is PTSnowWarp
    }
}
