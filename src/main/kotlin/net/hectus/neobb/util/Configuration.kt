package net.hectus.neobb.util

import net.hectus.neobb.NeoBB
import org.bukkit.configuration.file.FileConfiguration
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

object Configuration {
    enum class StructureMode(val pathSupplier: () -> Path) {
        LOCAL({ NeoBB.DATA_DIR.resolve("structures") }),
        GLOBAL({ File("~/.config/neobb-structures").toPath() }),
        SERVER(LOCAL.pathSupplier);
    }

    var CONFIG: FileConfiguration private set

    var STRUCTURE_MODE: StructureMode private set
    var PRODUCTION: Boolean private set
    var STARTING_PLAYERS: Int private set
    var DATABASE_ENABLED: Boolean private set
    var MAX_ARENA_HEIGHT: Int private set

    init {
        NeoBB.PLUGIN.saveDefaultConfig()
        CONFIG = NeoBB.PLUGIN.config

        STRUCTURE_MODE = StructureMode.valueOf(CONFIG.getString("structure-mode", "local")!!.uppercase())
        PRODUCTION = CONFIG.getBoolean("production")
        STARTING_PLAYERS = CONFIG.getInt("starting-players")
        DATABASE_ENABLED = CONFIG.getBoolean("database.enabled")
        MAX_ARENA_HEIGHT = CONFIG.getInt("max-arena-height", 9)

        try {
            Files.createDirectories(STRUCTURE_MODE.pathSupplier.invoke())
        } catch (e: IOException) {
            NeoBB.LOG.error("Could not access and create structure directory.", e)
        }
    }
}