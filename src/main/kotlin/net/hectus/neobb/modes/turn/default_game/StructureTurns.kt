package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.display.location
import com.marcpg.libpg.util.MinecraftTime
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.game.mode.DefaultGame
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.matrix.structure.Structure
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

abstract class StructureTurn(namespace: String) : Turn<PlacedStructure>(namespace) {
    abstract val staticStructure: StaticStructure
    val referenceStructure: Structure
        get() = staticStructure.structure

    override val event: TurnEvent = TurnEvent.STRUCTURE
    override val items: List<ItemStack>
        get() = referenceStructure.items()
    override val mainItem: ItemStack
        get() = items.first()
}

object TDaylightSensorLine : StructureTurn("daylight_sensor_line"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.REDSTONE
    override val cost: Int = 6

    override val staticStructure: StaticStructure = StaticStructures.Default.DAYLIGHT_SENSOR_LINE

    override val counters: List<CounterFilter> = listOf(TurnClazz.HOT, TurnClazz.NATURE, TurnClazz.WATER, TurnClazz.REDSTONE)

    override fun apply(exec: TurnExec<PlacedStructure>) {
        exec.game.time = MinecraftTime.DAY
    }
}

object TIronBarJail : StructureTurn("iron_bar_jail"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 7

    override val staticStructure: StaticStructure = StaticStructures.Default.IRON_BAR_JAIL

    // TODO: Set the `trapped` meta when creating TurnExec for TIronBarJail.
    override fun apply(exec: TurnExec<PlacedStructure>) {
        val trapped = exec.meta["trapped"]
        if (trapped !is NeoPlayer || trapped.location().distance(exec.data.lastBlock.location) > 1.5) return

        trapped.addModifier(Modifiers.Player.NO_MOVE)
        trapped.addModifier(Modifiers.Player.Default.NO_ATTACK)

        exec.game.turnScheduler.runTaskTimer(ScheduleID.IRON_BAR_JAIL, 1, {
            !trapped.hasModifier(Modifiers.Player.NO_MOVE) || !trapped.hasModifier(Modifiers.Player.Default.NO_ATTACK)
        }) {
            val last = exec.hist.last()
            if (last.turn is CounterFunction && TurnClazz.REDSTONE.doCounter(last) && TurnClazz.SUPERNATURAL.doCounter(last)) {
                trapped.removeModifier(Modifiers.Player.NO_MOVE)
                trapped.removeModifier(Modifiers.Player.Default.NO_ATTACK)
            }
        }
    }
}

object TOakDoorTurtling : StructureTurn("oak_door_turtling"), DefenseFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val maxAmount: Int = 1
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.OAK_DOOR_TURTLING

    // Not sure if it would be OAK_DOOR or some lower part thing with the automatic item detection based on the saved structure.
    override val items: List<ItemStack> = listOf(ItemStack.of(Material.OAK_DOOR, 4))

    override fun applyDefense(exec: TurnExec<*>) {
        exec.player.addModifier(Modifiers.Player.Default.DEFENDED)
        exec.game.turnScheduler.runTaskTimer(ScheduleID.OAK_DOOR_TURTLING, 1, {
            exec.player.hasModifier(Modifiers.Player.Default.DEFENDED)
        }) {
            val last = exec.hist.last()
            if (last.turn is CounterFunction && TurnClazz.HOT.doCounter(last) && TurnClazz.REDSTONE.doCounter(last)) {
                exec.player.removeModifier(Modifiers.Player.Default.DEFENDED)
            }
        }
    }
}

object TPumpkinWall : StructureTurn("pumpkin_wall"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NATURE
    override val cost: Int = 5

    override val staticStructure: StaticStructure = StaticStructures.Default.PUMPKIN_WALL

    override val buffs: List<Buff<*>> = listOf(Luck(10))

    override fun apply(exec: TurnExec<PlacedStructure>) {
        exec.game.time = MinecraftTime.MIDNIGHT
    }
}

object TRedstoneWall : StructureTurn("redstone_wall"), EventFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.REDSTONE
    override val cost: Int = 4

    override val staticStructure: StaticStructure = StaticStructures.Default.REDSTONE_WALL

    override fun triggerEvent(exec: TurnExec<*>) {
        exec.game.addModifier(Modifiers.Game.REDSTONE_POWER) // TODO: Power all blocks if this modifier is present.
        exec.game.turnScheduler.runTaskLater(ScheduleID.REDSTONE_POWER, 3) {
            exec.game.removeModifier(Modifiers.Game.REDSTONE_POWER)
        }
    }
}

abstract class GlassWallTurn(namespace: String) : StructureTurn(namespace), DefenseFunction, BuffFunction {
    override val mode: String = "default"
    override val maxAmount: Int = 1
    override val cost: Int = 5

    override val buffs: List<Buff<*>> = listOf(Luck(5))

    // TODO: Convert to TurnExec metadata:
    private var stay: Boolean = false

    override fun applyDefense(exec: TurnExec<*>) {
        exec.meta["stay"] = true
        exec.player.addModifier(Modifiers.Player.Default.DEFENDED)

        exec.game.turnScheduler.runTaskTimer(ScheduleID.GLASS_WALL_DEFENSE, 1, { stay }) {
            exec.meta["stay"] = Randomizer.boolByChance(if (exec.game is DefaultGame) 40.0 else 60.0)
            if (exec.meta["stay"] as Boolean)
                exec.player.addModifier(Modifiers.Player.Default.DEFENDED)
        }
    }
}

object TBlueGlassWall : GlassWallTurn("blue_glass_wall") {
    override val clazz: TurnClazz? = TurnClazz.WATER

    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.BLUE
}

object TGlassWall : GlassWallTurn("glass_wall") {
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL

    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.DEFAULT
}

object TGreenGlassWall : GlassWallTurn("green_glass_wall") {
    override val clazz: TurnClazz? = TurnClazz.NATURE

    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.GREEN
}

object TOrangeGlassWall : GlassWallTurn("orange_glass_wall") {
    override val clazz: TurnClazz? = TurnClazz.HOT

    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.ORANGE
}

object TPinkGlassWall : GlassWallTurn("pink_glass_wall") {
    override val clazz: TurnClazz? = TurnClazz.SUPERNATURAL

    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.PINK
}

object TRedGlassWall : GlassWallTurn("red_glass_wall") {
    override val clazz: TurnClazz? = TurnClazz.REDSTONE

    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.RED
}

object TWhiteGlassWall : GlassWallTurn("white_glass_wall") {
    override val clazz: TurnClazz? = TurnClazz.COLD

    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.WHITE
}
