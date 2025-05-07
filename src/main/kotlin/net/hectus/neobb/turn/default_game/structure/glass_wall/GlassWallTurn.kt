package net.hectus.neobb.turn.default_game.structure.glass_wall

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.game.mode.DefaultGame
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.structure.StructureManager
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.turn.default_game.attribute.function.DefenseFunction
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.hectus.neobb.util.Modifiers

abstract class GlassWallTurn(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), DefenseFunction, BuffFunction {
    override val cost: Int = 5
    override val referenceStructure: Structure
        get() = StructureManager[color().lowercase() + "_glass_wall"]!!

    private var stay: Boolean = false

    open fun color(): String = this::class.simpleName!!.removePrefix("T").removeSuffix("GlassWall").lowercase()

    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
        player.game.turnScheduler.runTaskTimer(ScheduleID.GLASS_WALL_DEFENSE, 1, { stay }) {
            stay = Randomizer.boolByChance(if (player.game is DefaultGame) 40.0 else 60.0)
            if (stay) player.addModifier(Modifiers.Player.Default.DEFENDED)
        }
    }

    override fun buffs(): List<Buff<*>> = listOf(Luck(5))
}