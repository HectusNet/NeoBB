package net.hectus.neobb.turn.legacy_game.structure

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.StaticStructure
import net.hectus.neobb.structure.StaticStructures
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.hectus.neobb.util.Modifiers
import org.bukkit.util.Vector

class LTRedstoneBlockWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), BuffFunction, NatureClazz {
    override val cost: Int = 6
    override val staticStructure: StaticStructure = StaticStructures.Legacy.REDSTONE_BLOCK_WALL

    override fun apply() {
        val nextPlayer = player!!.nextPlayer().player
        nextPlayer.velocity = nextPlayer.velocity.clone().add(Vector(0, 2, 0))

        player.game.addModifier(Modifiers.Game.Legacy.NO_ATTACK)
        player.game.turnScheduler.runTaskLater(ScheduleID.REDSTONE_BLOCK_WALL, 2) {
            player.game.removeModifier(Modifiers.Game.Legacy.NO_ATTACK)
        }
    }

    override fun buffs(): List<Buff<*>> = listOf(ExtraTurn(2))
}
