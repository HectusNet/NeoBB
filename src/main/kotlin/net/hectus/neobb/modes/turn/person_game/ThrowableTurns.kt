package net.hectus.neobb.modes.turn.person_game

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.modes.turn.default_game.ThrowableTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.entity.Projectile
import org.bukkit.entity.Snowman

class PTSnowball(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), SituationalAttackCategory {
    override val damage: Double = 2.0
    override fun apply() {
        ExtraTurn().invoke(player!!)
    }
    override fun unusable(): Boolean {
        return player!!.game.warp !is PTSnowWarp
    }
}

class PTSplashPotion(data: Projectile?, cord: Cord?, player: NeoPlayer?) : ThrowableTurn(data, cord, player), SituationalAttackCategory {
    override fun apply() {
        player!!.game.removeModifier(Modifiers.Game.Person.SNOW_GOLEM)
        player.game.world.getEntitiesByClass(Snowman::class.java).forEach { it.health = 0.0 }
    }
}
