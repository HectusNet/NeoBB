package net.hectus.neobb.modes.turn

import com.marcpg.libpg.lang.string
import com.marcpg.libpg.util.toTitleCase
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.modes.turn.default_game.attribute.AttackFunction
import net.hectus.neobb.modes.turn.default_game.attribute.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.CounterFunction
import net.hectus.neobb.modes.turn.default_game.attribute.TurnClazz
import net.hectus.neobb.modes.turn.person_game.AttackCategory
import net.hectus.neobb.modes.turn.person_game.CounterCategory
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

abstract class Turn<T>(
    val namespace: String
) {
    abstract val mode: String

    open val name: String = namespace.toTitleCase()

    open val mainItem: ItemStack = try {
        ItemStack.of(enumValueOf<Material>(namespace.uppercase()))
    } catch (e: IllegalArgumentException) {
        ItemStack.of(Material.RED_DYE)
    }

    open val items: List<ItemStack>
        get() = listOf(mainItem)

    open val maxAmount: Int = 2
    open val cost: Int? = null
    open val damage: Double? = null

    open val clazz: TurnClazz? = null
    abstract val event: TurnEvent

    open val counters: List<CounterFilter> = listOf()
    open val buffs: List<Buff<*>> = listOf()

    /**
     * All turns overriding this as `true` will be ignored like they were never used.
     * Used in templates for other turns, like a flower pot for flowers.
     */
    open val isCombo: Boolean = false

    open fun unusable(player: NeoPlayer): Boolean = false

    open fun apply(exec: TurnExec<T>) {}

    open fun goodChoice(player: NeoPlayer): Boolean {
        if (!player.game.started) return false

        if (unusable(player) || !player.game.allows(this)) return false

        if ((player.hasModifier(Modifiers.Player.Default.ATTACKED) || player.game.turnScheduler.exists(ScheduleID.FREEZE)) && !player.hasModifier(Modifiers.Player.Default.DEFENDED)) {
            if (player.game.history.isEmpty()) return true

            val last = player.game.history.last()

            if (this is CounterFunction) {
                return this.counters.any { it.doCounter(last) }
            } else if (this is CounterCategory) {
                return last.turn is AttackCategory && this in last.turn.counteredBy()
            }
        }

        return this !is AttackFunction || !player.targetPlayer().hasModifier(Modifiers.Player.Default.DEFENDED)
    }

    fun translation(locale: Locale): String = translationOrNull(locale) ?: name
    fun translationOrNull(locale: Locale): String? = locale.string("turns.$namespace").takeIf { it != "turns.$namespace" }

    fun itemMatches(material: Material) = items.any { it.type == material }
}
