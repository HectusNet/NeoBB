package net.hectus.neobb.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Utilities {
    public static @NotNull String camelToSnake(@NotNull String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public static @NotNull String counterFilterName(@NotNull String className) {
        return className.replaceAll("^T(?!.*(Function|Usage|Clazz)$)|Function$|Usage$|Clazz$", "").toLowerCase();
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
}
