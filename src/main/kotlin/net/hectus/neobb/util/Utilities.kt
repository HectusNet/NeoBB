package net.hectus.neobb.util

import com.marcpg.libpg.display.MinecraftReceiver
import com.marcpg.libpg.util.enumValueNoCase
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.EntityType.*
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import java.time.*
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

object Utilities {
    val ENTITY_TYPES: List<EntityType> = listOf(
        ALLAY, AXOLOTL, BAT, BEE, BLAZE, CAMEL, CAT, CAVE_SPIDER, CHICKEN, COW, CREEPER, DONKEY, DROWNED, ENDERMAN,
        ENDERMITE, EVOKER, FOX, FROG, GHAST, GLOW_SQUID, GOAT, GUARDIAN, HOGLIN, HORSE, HUSK, IRON_GOLEM, LLAMA,
        MAGMA_CUBE, MOOSHROOM, MULE, OCELOT, PANDA, PARROT, PHANTOM, PIG, PIGLIN, PILLAGER, POLAR_BEAR, RABBIT,
        RAVAGER, SHEEP, SHULKER, SILVERFISH, SKELETON, SKELETON_HORSE, SLIME, SNIFFER, SNOW_GOLEM, SPIDER, SQUID,
        STRAY, STRIDER, TRADER_LLAMA, TURTLE, VEX, VILLAGER, VINDICATOR, WANDERING_TRADER, WITCH, WITHER_SKELETON,
        WOLF, ZOGLIN, ZOMBIE, ZOMBIE_HORSE, ZOMBIE_VILLAGER, ZOMBIFIED_PIGLIN
    )

    private val EARLIEST_TIMEZONE: ZoneId = ZoneId.of("Pacific/Honolulu") // UTC−10
    private val LATEST_TIMEZONE: ZoneId = ZoneId.of("Pacific/Auckland") // UTC+12

    /**
     * Checks whether it's a weekday (Monday-Friday) in any major time zone.
     * This is inclusive, meaning it only requires one to be true to not cause any confusion amongst
     * international players. The time zones that are checked are from UTC-10 to UTC+12, because no major
     * population lives beyond that point. No, Kiritimati is not a major population center.
     * @return `true` if it is monday-friday, `false` if it is saturday or sunday.
     */
    fun isWeekday(): Boolean {
        return isWeekday(ZonedDateTime.now(EARLIEST_TIMEZONE).dayOfWeek) &&
                isWeekday(ZonedDateTime.now(LATEST_TIMEZONE).dayOfWeek)
    }

    fun isWeekday(day: DayOfWeek): Boolean = day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY

    /**
     * Checks if it is daytime (8am to 8pm) in the UTC time zone.
     * @return `true` if it is daytime, `false` if it is nighttime.
     */
    fun isDaytime(): Boolean {
        val time = ZonedDateTime.now(ZoneOffset.UTC).toLocalTime()
        return time.isAfter(LocalTime.of(8, 0)) && time.isBefore(LocalTime.of(20, 0))
    }

    fun clazz(clazz: KClass<out Turn<*>>): String = clazz.allSuperclasses.firstOrNull { it.simpleName!!.endsWith("Clazz") && it.simpleName!!.length > 5 }?.simpleName?.removeSuffix("Clazz")?.lowercase() ?: "other"
}

fun cancelEvent(event: Cancellable, eventPlayer: Player, requireStarted: Boolean, additionalPredicate: (NeoPlayer) -> Boolean) {
    playerEventAction(eventPlayer, requireStarted, additionalPredicate) { p ->
        event.isCancelled = true
        p.playSound(Sound.ENTITY_VILLAGER_NO, 0.5f)
    }
}

fun playerEventAction(eventPlayer: Player, requireStarted: Boolean, additionalPredicate: (NeoPlayer) -> Boolean = { true }, action: (NeoPlayer) -> Unit) {
    val player = GameManager.player(eventPlayer)
    if (player != null && (!requireStarted || player.game.started) && additionalPredicate.invoke(player))
        action.invoke(player)
}

fun KClass<out Turn<*>>.material(): Material = enumValueNoCase(simpleName!!.counterFilterName().camelToSnake())

private val COUNTER_FILTER_REGEX = Regex("^.?T(?!.*(Function|Usage|Clazz)$)|Function$|Usage$|Clazz$")
private val CAMEL_CASE_REGEX = Regex("([a-z])([A-Z]+)")

fun String.camelToSnake(): String = replace(CAMEL_CASE_REGEX, "$1_$2").lowercase()
fun String.camelToTitle(): String = replace(CAMEL_CASE_REGEX, "$1 $2")
fun String.counterFilterName(): String = replaceFirst(COUNTER_FILTER_REGEX, "")

fun MinecraftReceiver.eachNeoPlayer(action: (NeoPlayer) -> Unit) = eachReceiver {
    if (it is NeoPlayer)
        action(it)
}
