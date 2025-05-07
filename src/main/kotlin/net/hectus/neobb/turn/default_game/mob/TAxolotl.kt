package net.hectus.neobb.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.*
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.turn.default_game.warp.WarpTurn
import org.bukkit.entity.Axolotl
import kotlin.reflect.KClass

class TAxolotl(data: Axolotl?, cord: Cord?, player: NeoPlayer?) : MobTurn<Axolotl>(data, cord, player), BuffFunction, WaterClazz {
    override val cost: Int = 4

    override fun apply() {
        when (data!!.variant) {
            Axolotl.Variant.LUCY -> randomWarp(HotClazz::class)
            Axolotl.Variant.WILD -> randomWarp(NatureClazz::class)
            Axolotl.Variant.GOLD -> randomWarp(SupernaturalClazz::class)
            Axolotl.Variant.CYAN -> randomWarp(WaterClazz::class)
            Axolotl.Variant.BLUE -> player!!.game.win(player)
        }
    }

    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(10), Luck(-10, Buff.BuffTarget.OPPONENTS))
    }

    private fun randomWarp(allows: KClass<out Clazz>) {
        player!!.game.warp(Randomizer.fromCollection(player.shop.dummyTurns
                    .filter { it is WarpTurn && it.allows.contains(allows) }
                    .map { it as WarpTurn }))
    }
}
