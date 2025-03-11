package net.hectus.neobb.util;

import net.hectus.neobb.NeoBB;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public final class Configuration {
    public enum StructureMode {
        LOCAL(() -> NeoBB.DATA_DIR.resolve("structures")),
        GLOBAL(() -> new File("~/.config/neobb-structures").toPath()),
        SERVER(LOCAL.pathSupplier);

        public final Supplier<Path> pathSupplier;

        StructureMode(Supplier<Path> pathSupplier) {
            this.pathSupplier = pathSupplier;
        }

        public Path path() {
            return pathSupplier.get();
        }
    }

    public static FileConfiguration CONFIG;

    public static StructureMode STRUCTURE_MODE;
    public static boolean PRODUCTION;
    public static int STARTING_PLAYERS;
    public static boolean DATABASE_ENABLED;
    public static int MAX_ARENA_HEIGHT;

    public static void init(@NotNull JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        CONFIG = plugin.getConfig();

        STRUCTURE_MODE = StructureMode.valueOf(CONFIG.getString("structure-mode", "local").toUpperCase());
        PRODUCTION = CONFIG.getBoolean("production");
        STARTING_PLAYERS = CONFIG.getInt("starting-players");
        DATABASE_ENABLED = CONFIG.getBoolean("database.enabled");
        MAX_ARENA_HEIGHT = CONFIG.getInt("max-arena-height", 9);

        try {
            Files.createDirectories(STRUCTURE_MODE.path());
        } catch (IOException e) {
            NeoBB.LOG.error("Could not access and create structure directory.", e);
        }
    }
}
