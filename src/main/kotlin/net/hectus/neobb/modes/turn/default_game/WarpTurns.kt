package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Configuration
import net.hectus.neobb.util.Modifiers
import kotlin.reflect.KClass

abstract class WarpTurn(data: PlacedStructure?, cord: Cord, val name: String, player: NeoPlayer?) : StructureTurn(data, cord, player), WarpFunction {
    enum class Temperature { COLD, NORMAL, HOT }

    override val maxAmount: Int = 1

    abstract val chance: Double
    abstract val allows: List<KClass<out Clazz>>
    abstract val temperature: Temperature

    val lowCorner: Cord = Cord.ofList(Configuration.CONFIG.getIntegerList("warps.$name"))
    val center: Cord = lowCorner + Cord(4.5, 0.0, 4.5)
    val highCorner: Cord = lowCorner + Cord(9.0, Configuration.MAX_ARENA_HEIGHT.toDouble(), 9.0)

    open fun canBePlayed(): Boolean = true

    override fun apply() {
        if (canBePlayed() && (Randomizer.boolByChance(chance) || player!!.hasModifier(Modifiers.Player.Default.ALWAYS_WARP))) {
            player!!.removeModifier(Modifiers.Player.Default.ALWAYS_WARP)
            player.game.players.forEach { it.teleport(it.cord() - player.game.warp.lowCorner + lowCorner) }
            player.game.warp(this)
        }
    }
}

class TAmethystWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "amethyst", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.AMETHYST
    override val cost: Int = 4
    override val chance: Double = 20.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, WaterClazz::class, NatureClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}

class TCliffWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "cliff", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.CLIFF
    override val cost: Int = 4
    override val chance: Double = 60.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}

class TDefaultWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "default", player) {
    // Just anything that exists will be ignored anyways.
    override val staticStructure: StaticStructure = StaticStructures.Default.PUMPKIN_WALL
    override val chance: Double = 0.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, WaterClazz::class, NatureClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
    override fun canBePlayed(): Boolean = false
    override fun apply() {}
}

class TDesertWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "desert", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.DESERT
    override val cost: Int = 4
    override val chance: Double = 80.0
    override val allows: List<KClass<out Clazz>> = listOf(HotClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.HOT
}

class TEndWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "end", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.END
    override val cost: Int = 4
    override val chance: Double = 60.0
    override val allows: List<KClass<out Clazz>> = listOf(ColdClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.COLD
}

class TFrozenWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "frozen", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.FROZEN
    override val cost: Int = 4
    override val chance: Double = 70.0
    override val allows: List<KClass<out Clazz>> = listOf(ColdClazz::class, WaterClazz::class)
    override val temperature: Temperature = Temperature.COLD
}

class TMeadowWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "meadow", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.MEADOW
    override val cost: Int = 4
    override val chance: Double = 30.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, NatureClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}

class TMushroomWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "mushroom", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.MUSHROOM
    override val cost: Int = 4
    override val chance: Double = 50.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, NatureClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}

class TNerdWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "nerd", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.NERD
    override val cost: Int = 4
    override val chance: Double = 20.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, NatureClazz::class, RedstoneClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}

class TNetherWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "nether", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.NETHER
    override val cost: Int = 4
    override val chance: Double = 80.0
    override val allows: List<KClass<out Clazz>> = listOf(HotClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.HOT
}

class TOceanWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "ocean", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.OCEAN
    override val cost: Int = 4
    override val chance: Double = 70.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, WaterClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}

class TRedstoneWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "redstone", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.REDSTONE
    override val cost: Int = 4
    override val chance: Double = 60.0
    override val allows: List<KClass<out Clazz>> = listOf(HotClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.HOT
}

class TSunWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "sun", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.SUN
    override val cost: Int = 4
    override val chance: Double = 40.0
    override val allows: List<KClass<out Clazz>> = listOf(HotClazz::class, RedstoneClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.HOT
}

class TVoidWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "void", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.VOID
    override val cost: Int = 4
    override val chance: Double = 10.0
    override val allows: List<KClass<out Clazz>> = listOf(ColdClazz::class, NeutralClazz::class, WaterClazz::class, RedstoneClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.COLD
}

class TWoodWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?
                ) : WarpTurn(data, cord, "wood", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.WOOD
    override val cost: Int = 4
    override val chance: Double = 100.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, NatureClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}
