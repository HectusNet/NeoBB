package net.hectus.neobb.turn.person_game.warp

import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.util.Modifiers

class PTIceWarp(data: PlacedStructure?, player: NeoPlayer?) : PWarpTurn(data, "ice", player) {
    override val chance: Double = 50.0

    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.Person.BLUE_ENTITIES)
        player.game.turnScheduler.runTaskTimer(ScheduleID.ICE, 1) {
            player.game.players.forEach { it.damage(1.0) }
        }
        player.game.time = MinecraftTime.NOON
    }
}
