package net.hectus.neobb.modes.turn.person_game

import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.ThrowableTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.entity.Projectile
import org.bukkit.entity.Snowman

object PTSnowball : ThrowableTurn("snowball"), SituationalAttackCategory {
    override val mode: String = "person"
    override val damage: Double = 2.0

    override val buffs: List<Buff<*>> = listOf(ExtraTurn())

    override fun unusable(player: NeoPlayer): Boolean = player.game.warp !is PTSnowWarp
}

object PTSplashPotion : ThrowableTurn("splash_potion"), SituationalAttackCategory {
    override val mode: String = "person"

    override fun apply(exec: TurnExec<Projectile>) {
        exec.game.removeModifier(Modifiers.Game.Person.SNOW_GOLEM)
        exec.game.world.getEntitiesByClass(Snowman::class.java).forEach { it.health = 0.0 }
    }
}
