package net.hectus.neobb.turn.person_game.warp

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.StaticStructure
import net.hectus.neobb.structure.StaticStructures
import net.hectus.neobb.util.Modifiers

class PTIceWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "ice", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.ICE

    override val chance: Double = 50.0

    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.Person.BLUE_ENTITIES)
        player.game.turnScheduler.runTaskTimer(ScheduleID.ICE, 1) {
            player.game.players.forEach { it.damage(1.0) }
        }
        player.game.time = MinecraftTime.NOON
    }
}
