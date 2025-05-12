package net.hectus.neobb.turn.default_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.StaticStructure
import net.hectus.neobb.structure.StaticStructures
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import kotlin.reflect.KClass

class TWoodWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?
                ) : WarpTurn(data, cord, "wood", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.WOOD

    override val cost: Int = 4
    override val chance: Double = 100.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, NatureClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}
