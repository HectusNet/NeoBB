package net.hectus.neobb.modes.turn.person_game

import com.marcpg.libpg.util.MinecraftTime
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.StructureTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers

object PTCandleCircle : StructureTurn("candle_circle"), ArmorCategory {
    override val mode: String = "person"

    override val staticStructure: StaticStructure = StaticStructures.Person.CANDLE_CIRCLE

    override fun armor(): Int = 3
    override fun counteredBy(): List<Turn<*>> = listOf(PTArmorStand)
}

object PTPumpkinWall : StructureTurn("pumpkin_wall"), UtilityCategory {
    override val mode: String = "person"

    override val staticStructure: StaticStructure = StaticStructures.Person.PUMPKIN_WALL

    override fun apply(exec: TurnExec<PlacedStructure>) {
        exec.game.time = MinecraftTime.MIDNIGHT
    }
}

object PTStoneWall : StructureTurn("stone_wall"), ArmorCategory {
    override val mode: String = "person"

    override val staticStructure: StaticStructure = StaticStructures.Person.STONE_WALL

    override fun armor(): Int = 3
    override fun counteredBy(): List<Turn<*>> = listOf(PTStonecutter)
}

object PTTorchCircle : StructureTurn("torch_circle"), UtilityCategory {
    override val mode: String = "person"

    override val staticStructure: StaticStructure = StaticStructures.Person.TORCH_CIRCLE

    override fun unusable(player: NeoPlayer): Boolean = player.game.warp !is PTVillagerWarp

    override fun apply(exec: TurnExec<PlacedStructure>) {
        exec.player.addArmor(4.0)
    }
}

object PTTurtling : StructureTurn("turtling"), ArmorCategory {
    override val mode: String = "person"

    override val staticStructure: StaticStructure = StaticStructures.Person.TURTLING

    override fun armor(): Int = 3
    override fun counteredBy(): List<Turn<*>> = listOf(PTLever)
}

object PTWoodWall : StructureTurn("wood_wall"), DefensiveCategory {
    override val mode: String = "person"

    override val staticStructure: StaticStructure = StaticStructures.Person.WOOD_WALL

    override fun apply(exec: TurnExec<PlacedStructure>) {
        exec.player.heal(1.0)
        if (Randomizer.boolByChance(15.0))
            ExtraTurn().invoke(exec.player)
    }

    override fun applyDefense(exec: TurnExec<*>) {
        exec.player.addModifier(Modifiers.Player.Default.DEFENDED)
    }
}
