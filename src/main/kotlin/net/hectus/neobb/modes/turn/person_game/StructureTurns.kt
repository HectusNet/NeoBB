package net.hectus.neobb.modes.turn.person_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.StructureTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import kotlin.reflect.KClass

class PTCandleCircle(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), ArmorCategory {
    override val staticStructure: StaticStructure = StaticStructures.Person.CANDLE_CIRCLE
    override fun armor(): Int = 3
    override fun counteredBy(): List<KClass<out Turn<*>>> = listOf(PTArmorStand::class)
}

class PTPumpkinWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), UtilityCategory {
    override val staticStructure: StaticStructure = StaticStructures.Person.PUMPKIN_WALL
    override fun apply() {
        player!!.game.time = MinecraftTime.MIDNIGHT
    }
}

class PTStoneWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), ArmorCategory {
    override val staticStructure: StaticStructure = StaticStructures.Person.STONE_WALL
    override fun armor(): Int = 3
    override fun counteredBy(): List<KClass<out Turn<*>>> = listOf(PTStonecutter::class)
}

class PTTorchCircle(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), UtilityCategory {
    override val staticStructure: StaticStructure = StaticStructures.Person.TORCH_CIRCLE
    override fun apply() {
        player!!.addArmor(4.0)
    }
    override fun unusable(): Boolean {
        return player!!.game.warp !is PTVillagerWarp
    }
}

class PTTurtling(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), ArmorCategory {
    override val staticStructure: StaticStructure = StaticStructures.Person.TURTLING
    override fun armor(): Int = 3
    override fun counteredBy(): List<KClass<out Turn<*>>> = listOf(PTLever::class)
}

class PTWoodWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), DefensiveCategory {
    override val staticStructure: StaticStructure = StaticStructures.Person.WOOD_WALL
    override fun apply() {
        player!!.heal(1.0)
        if (Randomizer.boolByChance(15.0))
            ExtraTurn().invoke(player)
    }
    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
    }
}
