package net.hectus.neobb.util;

import org.jetbrains.annotations.NotNull;

public class Utilities {
    public static @NotNull String camelToSnake(@NotNull String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public static @NotNull String counterFilterName(@NotNull String className) {
        return className.replaceAll("^T(?!.*(Function|Usage|Clazz)$)|Function$|Usage$|Clazz$", "").toLowerCase();
    }
}
