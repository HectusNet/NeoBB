package net.hectus.neobb.util;

import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utilities {
    public static final Random RANDOM = new Random();
    public static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    public static @NotNull String camelToSnake(@NotNull String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public static @NotNull String counterFilterName(@NotNull String className) {
        return className.replaceAll("^T(?!.*(Function|Usage|Clazz)$)|Function$|Usage$|Clazz$", "");
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

    public static <T> void loop(T @NotNull [][][] array, boolean allowNull, TriConsumer<Integer, Integer, Integer> action) {
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[0].length; y++) {
                for (int z = 0; z < array[0][0].length; z++) {
                    if (allowNull || array[x][y][z] != null) action.accept(x, y, z);
                }
            }
        }
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
}
