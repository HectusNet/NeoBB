package net.hectus.neobb.util

import com.marcpg.libpg.data.time.Time
import com.marcpg.libpg.lang.Translation
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.storing.CordMinecraftAdapter
import net.hectus.neobb.NeoBB
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.Turn
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.*
import org.bukkit.entity.EntityType
import org.bukkit.entity.EntityType.*
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.scheduler.BukkitTask
import java.text.MessageFormat
import java.time.*
import java.util.*
import kotlin.reflect.KClass

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
}

inline fun <reified T : Enum<T>> enumValueNoCase(name: String): T = enumValues<T>().firstOrNull { it.name.equals(name, ignoreCase = true) }
    ?: throw IllegalArgumentException("No enum constant ${T::class.qualifiedName}.$name")

fun bukkitRun(task: (BukkitTask) -> Unit) = Bukkit.getScheduler().runTask(NeoBB.PLUGIN, task)
fun bukkitRunLater(delay: Time, task: (BukkitTask) -> Unit) = Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, task, delay.get() * 20)
fun bukkitRunLater(delay: Long, task: (BukkitTask) -> Unit) = Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, task, delay)
fun bukkitRunTimer(delay: Long, interval: Long, task: (BukkitTask) -> Unit) = Bukkit.getScheduler().runTaskTimer(NeoBB.PLUGIN, task, delay, interval)

fun <T> List<T>.following(element: T): T? {
    val index = indexOf(element)
    return if (index != -1 && index + 1 < size) this[index + 1] else null
}

fun Component.asString(): String = PlainTextComponentSerializer.plainText().serialize(this)

fun Locale.component(key: String, vararg variables: String?, color: TextColor? = null, decoration: TextDecoration? = null): Component {
    var component = Component.text(string(key, *variables))
    if (color != null)
        component = component.color(color)
    if (decoration != null)
        component = component.decorate(decoration)
    return component
}

fun Locale.string(key: String, vararg variables: String?): String {
    val plainString = Translation.string(this, key)
    return if (variables.isEmpty()) plainString else MessageFormat.format(plainString, *variables)
}

fun Location.asCord(): Cord = CordMinecraftAdapter.ofLocation(this)
fun Cord.asLocation(world: World): Location = CordMinecraftAdapter.toLocation(this, world)

private val NUMBER_REGEX = Regex("\\d+")
fun String.extractNumber(): Int = NUMBER_REGEX.find(this)?.value?.toInt() ?: 0

private val COUNTER_FILTER_REGEX = Regex("^.?T(?!.*(Function|Usage|Clazz)$)|Function$|Usage$|Clazz$")
private val CAMEL_CASE_REGEX = Regex("([a-z])([A-Z]+)")

fun String.makeCapitalized(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
fun String.camelToSnake(): String = replace(CAMEL_CASE_REGEX, "$1_$2").lowercase()
fun String.camelToTitle(): String = replace(CAMEL_CASE_REGEX, "$1 $2")
fun String.counterFilterName(): String = replace(COUNTER_FILTER_REGEX, "")

fun KClass<out Turn<*>>.material(): Material = enumValueNoCase(simpleName!!.counterFilterName().camelToSnake())

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
