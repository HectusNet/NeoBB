package net.hectus.neobb.util

import com.destroystokyo.paper.ParticleBuilder
import com.marcpg.libpg.display.MinecraftReceiver
import com.marcpg.libpg.display.playSound
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.toCord
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.player.NeoPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.EntityType.*
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.util.Vector
import java.time.*

object Utilities {
    val REMOVED_ENTITY_TYPES = listOf(ENDER_DRAGON, ELDER_GUARDIAN, GIANT, ILLUSIONER, RAVAGER, WITHER)

    val ENTITY_TYPES: List<EntityType> = EntityType.entries.filter { it.entityClass != null && it !in REMOVED_ENTITY_TYPES && LivingEntity::class.java.isAssignableFrom(it.entityClass!!) }

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

    fun generateBeam(start: Location, direction: Vector, length: Int, spacing: Double, particle: ParticleBuilder, forPoint: (Location) -> Unit = {}) {
        val dir = direction.clone().normalize().multiply(spacing)
        val current = start.clone()

        for (i in 0 until length) {
            current.add(dir)
            particle.location(current).spawn()
            forPoint(current)
        }
    }

    fun colorTransition(color1: TextColor, color2: TextColor, progress: Number): TextColor = TextColor.lerp(progress.toFloat(), color1, color2)
}

data class Bounds(
    val dimensions: Cord,
    val low: Cord,
    val high: Cord = low + dimensions,
    val center3D: Cord = low + (dimensions / Cord(2.0, 2.0, 2.0)),
    val center2D: Cord = Cord(center3D.x, low.y, center3D.z),
) {
    operator fun contains(cord: Cord): Boolean = cord.inBounds(low, high)
    operator fun contains(loc: Location): Boolean = contains(loc.toCord())
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

fun MinecraftReceiver.eachNeoPlayer(action: (NeoPlayer) -> Unit) = eachReceiver {
    if (it is NeoPlayer)
        action(it)
}

fun Component.noItalic(): Component = decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
