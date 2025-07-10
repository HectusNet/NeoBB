package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.game.mode.DefaultGame
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.matrix.structure.Structure
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

abstract class StructureTurn(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : Turn<PlacedStructure>(data, cord, player) {
    abstract val staticStructure: StaticStructure
    val referenceStructure: Structure
        get() = staticStructure.structure

    override fun items(): List<ItemStack> = referenceStructure.items()
}

class TDaylightSensorLine(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player),
    CounterbuffFunction, RedstoneClazz {
    override val cost: Int = 6
    override val staticStructure: StaticStructure = StaticStructures.Default.DAYLIGHT_SENSOR_LINE
    override fun apply() {
        player!!.game.time = MinecraftTime.DAY
    }
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(HotClazz::class),
            CounterFilter.clazz(NatureClazz::class),
            CounterFilter.clazz(WaterClazz::class),
            CounterFilter.clazz(RedstoneClazz::class)
        )
    }
    override fun buffs(): List<Buff<*>> = emptyList()
}

class TIronBarJail(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player),
    BuffFunction, NeutralClazz {
    override val cost: Int = 7
    override val staticStructure: StaticStructure = StaticStructures.Default.IRON_BAR_JAIL
    private val trapped: NeoPlayer?
        get() = player?.nextPlayer()
    override fun apply() {
        // This is not checking if the player is INSIDE it, but this is easier and works well enough.
        if (trapped == null || trapped!!.location().distance(data!!.lastBlock.location) > 1.5) return
        trapped!!.addModifier(Modifiers.Player.NO_MOVE)
        trapped!!.addModifier(Modifiers.Player.Default.NO_ATTACK)
        player!!.game.turnScheduler.runTaskTimer(ScheduleID.IRON_BAR_JAIL, 1, {
            !trapped!!.hasModifier(Modifiers.Player.NO_MOVE) || !trapped!!.hasModifier(Modifiers.Player.Default.NO_ATTACK)
        }) {
            if (player.game.history.last() is CounterFunction &&
                CounterFilter.clazz(RedstoneClazz::class).doCounter(player.game.history.last()) ||
                CounterFilter.clazz(SupernaturalClazz::class).doCounter(player.game.history.last())
            ) {
                trapped!!.removeModifier(Modifiers.Player.NO_MOVE)
                trapped!!.removeModifier(Modifiers.Player.Default.NO_ATTACK)
            }
        }
    }
    override fun buffs(): List<Buff<*>> = emptyList()
}

class TOakDoorTurtling(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player),
    DefenseFunction, NeutralClazz {
    override val maxAmount: Int = 1
    override val cost: Int = 4
    override val staticStructure: StaticStructure = StaticStructures.Default.OAK_DOOR_TURTLING
    override fun items(): List<ItemStack> {
        // Not sure if it would be OAK_DOOR or some lower part thing with the automatic item detection based on the saved structure.
        return listOf(ItemStack(Material.OAK_DOOR, 4))
    }
    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
        player.game.turnScheduler.runTaskTimer(ScheduleID.OAK_DOOR_TURTLING, 1, {
            player.hasModifier(Modifiers.Player.Default.DEFENDED)
        }) {
            if (player.game.history.last() is CounterFunction &&
                CounterFilter.clazz(HotClazz::class).doCounter(player.game.history.last()) ||
                CounterFilter.clazz(RedstoneClazz::class).doCounter(player.game.history.last())
            ) {
                player.removeModifier(Modifiers.Player.Default.DEFENDED)
            }
        }
    }
}

class TPumpkinWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player),
    BuffFunction, NatureClazz {
    override val cost: Int = 5
    override val staticStructure: StaticStructure = StaticStructures.Default.PUMPKIN_WALL
    override fun apply() {
        player!!.game.time = MinecraftTime.MIDNIGHT
    }
    override fun buffs(): List<Buff<*>> = listOf(Luck(10))
}

class TRedstoneWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player),
    EventFunction, RedstoneClazz {
    override val cost: Int = 4
    override val staticStructure: StaticStructure = StaticStructures.Default.REDSTONE_WALL
    override fun triggerEvent() {
        player!!.game.addModifier(Modifiers.Game.REDSTONE_POWER) // TODO: Power all blocks if this modifier is present.
        player.game.turnScheduler.runTaskLater(ScheduleID.REDSTONE_POWER, 3) {
            player.game.removeModifier(Modifiers.Game.REDSTONE_POWER)
        }
    }
}

abstract class GlassWallTurn(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player),
    DefenseFunction, BuffFunction {
    override val maxAmount: Int = 1
    override val cost: Int = 5

    private var stay: Boolean = false

    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
        player.game.turnScheduler.runTaskTimer(ScheduleID.GLASS_WALL_DEFENSE, 1, { stay }) {
            stay = Randomizer.boolByChance(if (player.game is DefaultGame) 40.0 else 60.0)
            if (stay) player.addModifier(Modifiers.Player.Default.DEFENDED)
        }
    }

    override fun buffs(): List<Buff<*>> = listOf(Luck(5))
}

class TBlueGlassWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : GlassWallTurn(data, cord, player),
    WaterClazz {
    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.BLUE
}

class TGlassWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : GlassWallTurn(data, cord, player),
    NeutralClazz {
    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.DEFAULT
}

class TGreenGlassWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : GlassWallTurn(data, cord, player),
    NatureClazz {
    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.GREEN
}

class TOrangeGlassWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : GlassWallTurn(data, cord, player),
    HotClazz {
    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.ORANGE
}

class TPinkGlassWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : GlassWallTurn(data, cord, player),
    SupernaturalClazz {
    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.PINK
}

class TRedGlassWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : GlassWallTurn(data, cord, player),
    RedstoneClazz {
    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.RED
}

class TWhiteGlassWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : GlassWallTurn(data, cord, player),
    ColdClazz {
    override val staticStructure: StaticStructure = StaticStructures.Default.GlassWall.WHITE
}
