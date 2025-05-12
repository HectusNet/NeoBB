package net.hectus.neobb.turn.person_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.StaticStructure
import net.hectus.neobb.structure.StaticStructures
import net.hectus.neobb.util.Modifiers
import org.bukkit.entity.Snowman

class PTSnowWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "snow", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.SNOW

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
