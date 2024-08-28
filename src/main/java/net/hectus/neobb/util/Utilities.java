package net.hectus.neobb.util;

import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public final class Utilities {
    public static @NotNull String camelToSnake(@NotNull String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public static @NotNull String counterFilterName(@NotNull String className) {
        return className.replaceAll("^T(?!.*(Function|Usage|Clazz)$)|Function$|Usage$|Clazz$", "");
    }

    public static @NotNull String turnKey(@NotNull String className) {
        return "turns." + camelToSnake(counterFilterName(className));
    }

    public static @NotNull String capitalizeFirstLetter(@NotNull String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
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
}
