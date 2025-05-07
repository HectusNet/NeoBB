package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.util.Modifiers
import org.bukkit.potion.PotionEffectType
import kotlin.reflect.KClass

class LTVoidWarp(data: PlacedStructure?, player: NeoPlayer?) : LWarpTurn(data, "void", player) {
    override val cost: Int = 4
    override val chance: Double = 30.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, ColdClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.COLD

    override fun apply() {
        Luck(15).apply(player!!)
        Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.OPPONENTS)
        player.opponents().forEach { it.addModifier(Modifiers.Player.NO_JUMP) }
    }
}
