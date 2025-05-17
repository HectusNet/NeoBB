package net.hectus.neobb

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marcpg.libpg.MinecraftLibPG
import com.marcpg.libpg.lang.Translation
import com.marcpg.libpg.storage.connection.AutoCatchingSQLConnection
import com.marcpg.libpg.storage.connection.DummySQLConnection
import com.marcpg.libpg.storage.connection.SQLConnection
import com.marcpg.libpg.util.ServerUtils
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.hectus.neobb.event.GameEvents
import net.hectus.neobb.event.PlayerEvents
import net.hectus.neobb.event.TurnEvents
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.matrix.structure.StructureManager
import net.hectus.neobb.util.Configuration
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.slf4j.Logger
import xyz.xenondevs.invui.InvUI
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Path
import java.util.*

class NeoBB : JavaPlugin() {
    companion object {
        lateinit var PLUGIN: NeoBB private set
        lateinit var LOG: Logger private set
        lateinit var DATA_DIR: Path private set
        lateinit var DATABASE: AutoCatchingSQLConnection<UUID> private set

        const val VERSION = "0.1.0"
    }

    override fun onEnable() {
        PLUGIN = this
        LOG = slF4JLogger
        DATA_DIR = dataPath

        MinecraftLibPG.init(this)
        InvUI.getInstance().setPlugin(this)

        translations()
        connectDatabase()

        StructureManager.load()

        ServerUtils.registerEvents(GameEvents(), PlayerEvents(), TurnEvents())
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(Commands.games(), "Manage the currently running games.", listOf("block-battle", "neobb"))
            event.registrar().register(Commands.giveup(), "Give up this game.", listOf("surrender"))
            event.registrar().register(Commands.structure(), "Manage the NeoBB structures.", listOf("neobb-structure"))
            event.registrar().register(Commands.debug(), "Test/debug some NeoBB features.")
        }
    }

    override fun onDisable() {
        GameManager.GAMES.values.forEach { it.draw(true) }
        Bukkit.unloadWorld("world", false) // Prevent the saving of the worlds.
        DATABASE.closeConnection()
    }

    private fun translations() {
        runCatching {
            val request = HttpRequest.newBuilder(URI("https://marcpg.com/neobb/lang/all")).GET().build()
            val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body()
            Translation.loadMaps(Gson().fromJson(response, object : TypeToken<Map<Locale?, Map<String?, String?>?>?>() {}.type))
        }.onFailure {
            LOG.error("Could not retrieve translations from https://marcpg.com/neobb/lang/all - NeoBB will continue to work as usual, just without translations.")

            // Backup default translations:
            val properties = Properties()
            properties.load(this.javaClass.classLoader.getResourceAsStream("en_US.properties")!!)
            Translation.loadSingleProperties(Locale.getDefault(), properties)
        }
    }

    private fun connectDatabase() {
        if (Configuration.DATABASE_ENABLED) {
            try {
                DATABASE = AutoCatchingSQLConnection(
                    SQLConnection.DatabaseType.POSTGRESQL,
                    Configuration.CONFIG.getString("database.address")!!,
                    Configuration.CONFIG.getInt("database.port", 0),
                    Configuration.CONFIG.getString("database.database", "hectus"),
                    Configuration.CONFIG.getString("database.user")!!,
                    Configuration.CONFIG.getString("database.passwd")!!,
                    Configuration.CONFIG.getString("database.table", "neobb_playerdata"),
                    "uuid"
                ) { LOG.error("Database error.", it) }
                LOG.info("Connected to real database.")
                return
            } catch (e: Exception) {
                LOG.error("Could not establish connection to database! Using a dummy database instead.", e)
            }
        }
        LOG.info("Connected to dummy database.")
        DATABASE = DummySQLConnection("table", "uuid")
    }
}
