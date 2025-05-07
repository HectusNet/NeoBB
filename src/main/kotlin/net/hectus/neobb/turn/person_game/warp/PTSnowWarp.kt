package net.hectus.neobb.turn.person_game.warp

import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.util.Modifiers
import org.bukkit.entity.Snowman

class PTSnowWarp(data: PlacedStructure?, player: NeoPlayer?) : PWarpTurn(data, "snow", player) {
    override val chance: Double = 40.0

    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.Person.WHITE_ENTITIES)

        player.game.world.spawn(player.location(), Snowman::class.java)
        player.game.addModifier(Modifiers.Game.Person.SNOW_GOLEM)
        player.game.turnScheduler.runTaskTimer(ScheduleID.SNOW_GOLEM, 1, { player.game.hasModifier(Modifiers.Game.Person.SNOW_GOLEM) }) {
            player.opponents().forEach { it.damage(1.0) }
        }
    }
}
