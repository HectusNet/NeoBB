package net.hectus.neobb.util

import com.google.common.base.Optional
import com.marcpg.libpg.config.*
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.toLocation
import net.hectus.neobb.NeoBB
import net.hectus.neobb.Registry
import net.hectus.neobb.game.mode.DefaultGame
import net.hectus.neobb.game.util.GameDifficulty
import org.bukkit.GameMode
import org.bukkit.World
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createDirectories

object Configuration : Config(PaperConfigProvider()) {
    override val versionHistory: List<ConfigVersion> = listOf()
    override val version: Int = 2

    var offline by boolean("offline")
    var structureMode by enum<StructureMode>("structure-mode", StructureMode.LOCAL)
    var maxArenaHeight by int("max-arena-height", 9)
    var restartAfterGame by boolean("restart-after-game")
    var endingCommands by custom("ending-commands", NeoEntryTypes.placeholder.list, listOf())
    var respawnAtConfig by boolean("respawn-at-config")

    var spawnGameMode by enum<GameMode>("player-spawn.game-mode", GameMode.ADVENTURE)
    var spawnWorld by custom("player-spawn.world", PaperEntryTypes.world, Optional.absent())
    var spawnCord by custom("player-spawn.location", PaperEntryTypes.cord, Cord(0.0, -64.0, 0.0))

    var queueEnabled by boolean("queue.enabled")
    var queueMinPlayers by int("queue.min-players", 2)
    var queueMaxPlayers by int("queue.max-players", 5)
    var queueCheckIntervalSecs by int("queue.check-interval", 15)
    var queueMethod by enum<QueueMethod>("queue.method", QueueMethod.COMMAND)
    var queueMode by custom("queue.mode", NeoEntryTypes.registry { Registry.modes }, DefaultGame)
    var queueDifficulty by enum<GameDifficulty>("queue.difficulty", GameDifficulty.NORMAL)
    var queueWorldName by custom("queue.world", NeoEntryTypes.placeholder, PlaceholderNameGetter("game-{id}"))
    var queueCord by custom("queue.location", PaperEntryTypes.cord, Cord(0.0, -64.0, 0.0))
    var queuePreCommands by custom("queue.pre-commands", NeoEntryTypes.placeholder.list, listOf())
    var queuePostCommands by custom("queue.post-commands", NeoEntryTypes.placeholder.list, listOf())

    var databaseEnabled by boolean("database.enabled")
    var databaseAddress by string("database.address", "localhost")
    var databasePort by int("database.port", 0)
    var databaseName by string("database.database", "database_name")
    var databaseTable by string("database.table", "table_name")
    var databaseUser by string("database.user", "username")
    var databasePassword by string("database.passwd", "password")

    val initialSpawnLocation get() = Cord.ofList(provider.getIntList("warps.default")!!) + Cord(4.5, 0.0, 4.5)
    val spawnLocation get() = if (spawnCord.y == -64.0) spawnWorld.get().spawnLocation else spawnCord.toLocation(spawnWorld.get())
    val queueCheckInterval get() = queueCheckIntervalSecs * 20
    fun queueLocation(world: World) = if (queueCord.y == -64.0) world.spawnLocation else queueCord.toLocation(world)

    fun init() {
        when (load()) {
            ConfigLoadResult.LOADED -> NeoBB.LOG.info("Configuration loaded.")
            ConfigLoadResult.CREATED -> NeoBB.LOG.info("Configuration has been created.")
            ConfigLoadResult.UPDATED -> {
                NeoBB.LOG.warn("============================= ! NOTE ! =========================")
                NeoBB.LOG.warn("| The config has been updated and may need to be reconfigured. |")
                NeoBB.LOG.warn("|    The old config has been backed up as 'config.yml.old'.    |")
                NeoBB.LOG.warn("================================================================")
            }
            ConfigLoadResult.UPDATED_AND_MIGRATED -> {
                NeoBB.LOG.info("============================= ! NOTE ! =======================")
                NeoBB.LOG.info("| The config has been updated and was successfully migrated. |")
                NeoBB.LOG.info("|   The old config has been backed up as 'config.yml.old'.   |")
                NeoBB.LOG.info("==============================================================")
            }
        }

        runCatching {
            structureMode.pathSupplier().createDirectories()
        }.onFailure {
            NeoBB.LOG.error("Could not access and create structure directory", it)
        }
    }
}

object NeoEntryTypes {
    val placeholder = CustomEntryType(
        BaseEntryTypes.string,
        { PlaceholderNameGetter(it) },
        { it.base }
    )

    fun <T> registry(entries: () -> Map<String, T>) = CustomEntryType(
        BaseEntryTypes.string,
        { entries()[it]!! },
        { entries().entries.first { e -> e.value == it }.key }
    )
}

enum class StructureMode(val pathSupplier: () -> Path) {
    LOCAL({ NeoBB.DATA_DIR.resolve("structures") }),
    GLOBAL({ File("~/.config/neobb-structures").toPath() }),
    SERVER(LOCAL.pathSupplier);
}

enum class QueueMethod { COMMAND, AUTO }
