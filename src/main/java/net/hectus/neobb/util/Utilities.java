package net.hectus.neobb.util;

import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.time.*;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.entity.EntityType.*;

public final class Utilities {
    public static final List<EntityType> ENTITY_TYPES = List.of(
            WITHER_SKELETON, STRAY, HUSK, ZOMBIE_VILLAGER, SKELETON_HORSE, ZOMBIE_HORSE, DONKEY, MULE, EVOKER, VEX,
            VINDICATOR, CREEPER, SKELETON, SPIDER, ZOMBIE, SLIME, GHAST, ZOMBIFIED_PIGLIN, ENDERMAN, CAVE_SPIDER,
            SILVERFISH, BLAZE, MAGMA_CUBE, BAT, WITCH, ENDERMITE, GUARDIAN, SHULKER, PIG, SHEEP, COW, CHICKEN, SQUID,
            WOLF, MOOSHROOM, SNOW_GOLEM, OCELOT, IRON_GOLEM, HORSE, RABBIT, POLAR_BEAR, LLAMA, PARROT, VILLAGER, TURTLE,
            PHANTOM, DROWNED, CAT, PANDA, PILLAGER, RAVAGER, TRADER_LLAMA, WANDERING_TRADER, FOX, BEE, HOGLIN, PIGLIN,
            STRIDER, ZOGLIN, AXOLOTL, GLOW_SQUID, GOAT, ALLAY, FROG, CAMEL, SNIFFER
    );

    public static final Random RANDOM = new Random();
    public static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    public static @NotNull String camelToSnake(@NotNull String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public static @NotNull String counterFilterName(@NotNull String className) {
        return className.replaceAll("^.?T(?!.*(Function|Usage|Clazz)$)|Function$|Usage$|Clazz$", "");
    }

    public static @NotNull String turnName(@NotNull String className, Locale locale) {
        return Translation.string(locale, "turns." + camelToSnake(counterFilterName(className)));
    }

    public static int extractNumber(String input) {
        Matcher matcher = NUMBER_PATTERN.matcher(input);
        if (matcher.find())
            return Integer.parseInt(matcher.group());
        return 0;
    }

    public static @NotNull Location listToLocation(World world, @NotNull List<Integer> list) {
        return new Location(world, list.get(0), list.get(1), list.get(2));
    }

    public static <T> void loop(T @NotNull [][][] array, boolean allowNull, Consumer<T> action) {
        for (T[][] vX : array) {
            for (T[] vY : vX) {
                for (T v : vY) {
                    if (allowNull || v != null) action.accept(v);
                }
            }
        }
    }

    public static void cancelEvent(@NotNull Cancellable event, Player eventPlayer, boolean started, Predicate<NeoPlayer> additionalPredicate) {
        playerEventAction(eventPlayer, started, additionalPredicate, p -> {
            event.setCancelled(true);
            p.player.playSound(p.player, Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
        });
    }

    public static void playerEventAction(Player eventPlayer, boolean started, Predicate<NeoPlayer> additionalPredicate, Consumer<NeoPlayer> action) {
        NeoPlayer player = GameManager.player(eventPlayer, true);
        if (player != null && (!started || player.game.started()) && additionalPredicate.test(player))
            action.accept(player);
    }

    private static final ZoneId EARLIEST_TIMEZONE = ZoneId.of("Pacific/Honolulu");  // UTC−10
    private static final ZoneId LATEST_TIMEZONE = ZoneId.of("Pacific/Auckland"); // UTC+12

    /**
     * Checks whether it's a weekday (monday-friday) in any major time zone.
     * This is inclusive, meaning it only requires one to be true, to not cause any confusion amongst
     * international players. The time zones that are checked are from UTC-10 to UTC+12, because no major
     * population lives beyond that point. No, Kiritimati is not a major population center.
     * @return {@code true} if it is monday-friday, {@code false} if it is saturday or sunday.
     */
    public static boolean isWeekday() {
        return isWeekday(ZonedDateTime.now(EARLIEST_TIMEZONE).getDayOfWeek()) &&
                isWeekday(ZonedDateTime.now(LATEST_TIMEZONE).getDayOfWeek());
    }

    public static boolean isWeekday(DayOfWeek day) {
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    /**
     * Checks if it is daytime (8am to 8pm) in the UTC time zone.
     * @return {@code true} if it is daytime, {@code false} if it is nighttime.
     */
    public static boolean isDaytime() {
        LocalTime time = ZonedDateTime.now(ZoneOffset.UTC).toLocalTime();
        return time.isAfter(LocalTime.of(8, 0)) && time.isBefore(LocalTime.of(20, 0));
    }
}
