package net.hectus.neobb.modes.turn.legacy_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.potion.PotionEffectType
import kotlin.reflect.KClass

class LTVoidWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : LWarpTurn(data, cord, "void", player) {
    override val staticStructure: StaticStructure = StaticStructures.Legacy.Warp.VOID

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
