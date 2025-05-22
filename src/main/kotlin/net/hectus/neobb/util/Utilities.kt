package net.hectus.neobb.util

import org.bukkit.entity.EntityType
import org.bukkit.entity.EntityType.*
import java.time.*

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

