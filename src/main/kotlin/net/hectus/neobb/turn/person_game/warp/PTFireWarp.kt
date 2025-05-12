package net.hectus.neobb.turn.person_game.warp

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.StaticStructure
import net.hectus.neobb.structure.StaticStructures
import net.hectus.neobb.util.Modifiers

class PTFireWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "fire", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.FIRE

    override val chance: Double = 50.0

    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.Person.FIRE_ENTITIES)
        player.game.turnScheduler.runTaskTimer(ScheduleID.BURN, 1) {
            player.game.players.forEach { it.damage(1.0) }
        }
        player.game.time = MinecraftTime.NOON
    }
}
