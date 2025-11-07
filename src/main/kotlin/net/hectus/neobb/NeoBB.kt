package net.hectus.neobb

import com.marcpg.libpg.init.KotlinPlugin
import com.marcpg.libpg.init.KotlinPluginCompanion
import com.marcpg.libpg.util.ServerUtils
import com.marcpg.storage.connection.AutoCatchingSQLConnection
import com.marcpg.storage.connection.DatabaseInfo
import com.marcpg.storage.connection.DummySQLConnection
import com.marcpg.storage.connection.SQLConnection
import net.hectus.neobb.event.GameEvents
import net.hectus.neobb.event.PlayerEvents
import net.hectus.neobb.event.TurnEvents
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.matrix.structure.StructureManager
import net.hectus.neobb.util.Configuration
import org.bukkit.Bukkit
import xyz.xenondevs.invui.InvUI
import java.net.URI
import java.util.*

class NeoBB : KotlinPlugin(Companion) {
    companion object : KotlinPluginCompanion() {
        lateinit var PLUGIN: NeoBB
        lateinit var DATABASE: AutoCatchingSQLConnection<UUID>

        override val VERSION: String = "0.2.1"
    }

    override fun enable() {
        PLUGIN = this

        InvUI.getInstance().setPlugin(this)

        loadTranslations(URI(if (Configuration.OFFLINE) "offline://mode.is/enabled" else "https://marcpg.com/neobb/lang/all"))
        connectDatabase()

        StructureManager.load()

        addListeners(GameEvents, PlayerEvents, TurnEvents)
        addCommands(
            ServerUtils.Cmd(Commands.debug, "Test/debug some NeoBB features."),
            ServerUtils.Cmd(Commands.game, "Manage the currently running games.", "block-battle", "neobb"),
            ServerUtils.Cmd(Commands.giveup, "Give up this game.", "surrender"),
            ServerUtils.Cmd(Commands.structure, "Manage the NeoBB structures.", "neobb-structure"),
        )

        Registry.load()
    }

    override fun disable() {
        GameManager.GAMES.values.forEach { it.draw(true) }
        Bukkit.unloadWorld("world", false) // Prevent the saving of the worlds.
        DATABASE.closeConnection()
    }

    private fun connectDatabase() {
        if (!Configuration.OFFLINE && Configuration.DATABASE_ENABLED) {
            try {
                DATABASE = AutoCatchingSQLConnection(
                    DatabaseInfo(
                        SQLConnection.DatabaseType.POSTGRESQL,
                        Configuration.CONFIG.getString("database.address")!!,
                        Configuration.CONFIG.getInt("database.port", 0),
                        Configuration.CONFIG.getString("database.database", "hectus") ?: "hectus",
                        Configuration.CONFIG.getString("database.user")!!,
                        Configuration.CONFIG.getString("database.passwd")!!
                    ),
                    Configuration.CONFIG.getString("database.table", "neobb_playerdata") ?: "neobb_playerdata",
                    "uuid"
                )
                DATABASE.exceptionHandling = { LOG.error("Database error.", it) }
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
