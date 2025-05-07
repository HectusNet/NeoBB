package net.hectus.neobb.turn.default_game.warp

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.structure.StructureManager
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.function.WarpFunction
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.hectus.neobb.util.Configuration
import net.hectus.neobb.util.Modifiers
import kotlin.reflect.KClass

abstract class WarpTurn(data: PlacedStructure?, val name: String, player: NeoPlayer?) : StructureTurn(data, Cord.ofList(Configuration.CONFIG.getIntegerList("warps.$name")), player), WarpFunction {
    enum class Temperature { COLD, NORMAL, HOT }

    override val requiresUsageGuide: Boolean = true
    override val maxAmount: Int = 1

    abstract val chance: Double
    abstract val allows: List<KClass<out Clazz>>
    abstract val temperature: Temperature

    val center: Cord = cord!!.add(Cord(5.0, 0.0, 5.0))
    val highCorner: Cord = cord!!.add(Cord(9.0, Configuration.MAX_ARENA_HEIGHT.toDouble(), 9.0))

    override val referenceStructure: Structure
        get() = StructureManager["$name-warp"]!!

    open fun canBePlayed(): Boolean = true

    override fun apply() {
        if (canBePlayed() && (Randomizer.boolByChance(chance) || player!!.hasModifier(Modifiers.Player.Default.ALWAYS_WARP))) {
            player!!.removeModifier(Modifiers.Player.Default.ALWAYS_WARP)
            player.game.players.forEach { it.teleport(it.cord().subtract(player.game.warp.cord!!).add(cord!!)) }
            player.game.warp(this)
        }
    }
}
