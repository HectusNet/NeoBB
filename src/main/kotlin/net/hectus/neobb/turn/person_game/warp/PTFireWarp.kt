package net.hectus.neobb.turn.person_game.warp

import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.util.Modifiers

class PTFireWarp(data: PlacedStructure?, player: NeoPlayer?) : PWarpTurn(data, "fire", player) {
    override val chance: Double = 50.0

    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.Person.FIRE_ENTITIES)
        player.game.turnScheduler.runTaskTimer(ScheduleID.BURN, 1) {
            player.game.players.forEach { it.damage(1.0) }
        }
        player.game.time = MinecraftTime.NOON
    }
}
