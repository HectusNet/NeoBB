package net.hectus.neobb.turn.default_game.warp

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.function.WarpFunction
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.hectus.neobb.util.Configuration
import net.hectus.neobb.util.Modifiers
import kotlin.reflect.KClass

abstract class WarpTurn(data: PlacedStructure?, cord: Cord, val name: String, player: NeoPlayer?) : StructureTurn(data, cord, player), WarpFunction {
    enum class Temperature { COLD, NORMAL, HOT }

    override val requiresUsageGuide: Boolean = true
    override val maxAmount: Int = 1

    abstract val chance: Double
    abstract val allows: List<KClass<out Clazz>>
    abstract val temperature: Temperature

    val lowCorner: Cord = Cord.ofList(Configuration.CONFIG.getIntegerList("warps.$name"))
    val center: Cord = lowCorner.add(Cord(4.5, 0.0, 4.5))
    val highCorner: Cord = lowCorner.add(Cord(9.0, Configuration.MAX_ARENA_HEIGHT.toDouble(), 9.0))

    open fun canBePlayed(): Boolean = true

    override fun apply() {
        if (canBePlayed() && (Randomizer.boolByChance(chance) || player!!.hasModifier(Modifiers.Player.Default.ALWAYS_WARP))) {
            player!!.removeModifier(Modifiers.Player.Default.ALWAYS_WARP)
            player.game.players.forEach { it.teleport(it.cord().subtract(player.game.warp.lowCorner).add(lowCorner)) }
            player.game.warp(this)
        }
    }
}
