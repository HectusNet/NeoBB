package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.display.teleport
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.attribute.TurnClazz
import net.hectus.neobb.modes.turn.default_game.attribute.WarpFunction
import net.hectus.neobb.util.Bounds
import net.hectus.neobb.util.Configuration
import net.hectus.neobb.util.Modifiers

abstract class WarpTurn(namespace: String) : StructureTurn(namespace), WarpFunction {
    enum class Temperature { COLD, NORMAL, HOT }

    override val maxAmount: Int = 1

    abstract val chance: Double
    abstract val allows: List<TurnClazz>
    abstract val temperature: Temperature

    val bounds: Bounds = Bounds(
        dimensions = Cord(9.0, Configuration.MAX_ARENA_HEIGHT.toDouble(), 9.0),
        low = Cord.ofList(Configuration.CONFIG.getIntegerList("warps.${namespace.removeSuffix("_warp")}"))
    )

    open fun canBePlayed(exec: TurnExec<PlacedStructure>): Boolean = true

    override fun apply(exec: TurnExec<PlacedStructure>) {
        if (canBePlayed(exec) && (Randomizer.boolByChance(chance) || exec.player.hasModifier(Modifiers.Player.Default.ALWAYS_WARP))) {
            exec.player.removeModifier(Modifiers.Player.Default.ALWAYS_WARP)
            exec.game.players.forEach { it.teleport(it.cord() - exec.game.warp.bounds.low + bounds.low) }
            exec.game.warp(exec.player, this)
        }
    }
}

object TAmethystWarp : WarpTurn("amethyst_warp") {
    override val mode: String = "default"
    override val cost: Int = 4
    
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.AMETHYST
    
    override val chance: Double = 20.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.NEUTRAL, TurnClazz.WATER, TurnClazz.NATURE, TurnClazz.SUPERNATURAL)
    override val temperature: Temperature = Temperature.NORMAL
}

object TCliffWarp : WarpTurn("cliff_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.CLIFF

    override val chance: Double = 60.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.NEUTRAL, TurnClazz.REDSTONE)
    override val temperature: Temperature = Temperature.NORMAL
}

object TDefaultWarp : WarpTurn("default_warp") {
    override val mode: String = "default"
    override val event: TurnEvent = TurnEvent.NONE

    // Just anything that exists will be ignored anyways.
    override val staticStructure: StaticStructure = StaticStructures.Default.PUMPKIN_WALL

    override val chance: Double = 0.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.NEUTRAL, TurnClazz.WATER, TurnClazz.NATURE)
    override val temperature: Temperature = Temperature.NORMAL

    override fun canBePlayed(exec: TurnExec<PlacedStructure>): Boolean = false
    override fun apply(exec: TurnExec<PlacedStructure>) {}
}

object TDesertWarp : WarpTurn("desert_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.DESERT

    override val chance: Double = 80.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.HOT, TurnClazz.REDSTONE)
    override val temperature: Temperature = Temperature.HOT
}

object TEndWarp : WarpTurn("end_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.END

    override val chance: Double = 60.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.COLD, TurnClazz.SUPERNATURAL)
    override val temperature: Temperature = Temperature.COLD
}

object TFrozenWarp : WarpTurn("frozen_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.FROZEN

    override val chance: Double = 70.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.COLD, TurnClazz.WATER)
    override val temperature: Temperature = Temperature.COLD
}

object TMeadowWarp : WarpTurn("meadow_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.MEADOW

    override val chance: Double = 30.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.NEUTRAL, TurnClazz.NATURE, TurnClazz.SUPERNATURAL)
    override val temperature: Temperature = Temperature.NORMAL
}

object TMushroomWarp : WarpTurn("mushroom_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.MUSHROOM

    override val chance: Double = 50.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.NEUTRAL, TurnClazz.NATURE, TurnClazz.SUPERNATURAL)
    override val temperature: Temperature = Temperature.NORMAL
}

object TNerdWarp : WarpTurn("nerd_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.NERD

    override val chance: Double = 20.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.NEUTRAL, TurnClazz.NATURE, TurnClazz.REDSTONE, TurnClazz.SUPERNATURAL)
    override val temperature: Temperature = Temperature.NORMAL
}

object TNetherWarp : WarpTurn("nether_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.NETHER

    override val chance: Double = 80.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.HOT, TurnClazz.REDSTONE)
    override val temperature: Temperature = Temperature.HOT
}

object TOceanWarp : WarpTurn("ocean_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.OCEAN

    override val chance: Double = 70.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.NEUTRAL, TurnClazz.WATER)
    override val temperature: Temperature = Temperature.NORMAL
}

object TRedstoneWarp : WarpTurn("redstone_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.REDSTONE

    override val chance: Double = 60.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.HOT, TurnClazz.REDSTONE)
    override val temperature: Temperature = Temperature.HOT
}

object TSunWarp : WarpTurn("sun_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.SUN

    override val chance: Double = 40.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.HOT, TurnClazz.REDSTONE, TurnClazz.SUPERNATURAL)
    override val temperature: Temperature = Temperature.HOT
}

object TVoidWarp : WarpTurn("void_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.VOID

    override val chance: Double = 10.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.COLD, TurnClazz.NEUTRAL, TurnClazz.WATER, TurnClazz.REDSTONE, TurnClazz.SUPERNATURAL)
    override val temperature: Temperature = Temperature.COLD
}

object TWoodWarp : WarpTurn("wood_warp") {
    override val mode: String = "default"
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.WOOD

    override val chance: Double = 100.0
    override val allows: List<TurnClazz> = listOf(TurnClazz.NEUTRAL, TurnClazz.NATURE)
    override val temperature: Temperature = Temperature.NORMAL
}
