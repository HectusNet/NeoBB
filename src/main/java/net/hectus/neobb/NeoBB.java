package net.hectus.neobb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marcpg.libpg.MinecraftLibPG;
import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.storage.connection.AutoCatchingSQLConnection;
import com.marcpg.libpg.storage.connection.DummySQLConnection;
import com.marcpg.libpg.storage.connection.SQLConnection;
import com.marcpg.libpg.storing.tuple.triple.Triad;
import com.marcpg.libpg.util.ServerUtils;
import net.hectus.neobb.event.GameEvents;
import net.hectus.neobb.event.PlayerEvents;
import net.hectus.neobb.event.TurnEvents;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.util.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import xyz.xenondevs.invui.InvUI;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public final class NeoBB extends JavaPlugin {
    public static final String VERSION = "0.0.9";

    public static NeoBB PLUGIN;
    public static Logger LOG;
    public static Path DATA_DIR;
    public static AutoCatchingSQLConnection<UUID> DATABASE;

    @Override
    public void onEnable() {
        PLUGIN = this;
        LOG = getSLF4JLogger();
        DATA_DIR = getDataPath();

        Configuration.init(this);
        MinecraftLibPG.init(this);
        InvUI.getInstance().setPlugin(this);
        StructureManager.load();

        translations();
        connectDatabase();

        ServerUtils.registerEvents(new GameEvents(), new PlayerEvents(), new TurnEvents());
        ServerUtils.registerCommands(Triad.of(
                "Starts a new game.", Commands.gamesCommand(), List.of("match", "round"),
                "Gives up.", Commands.giveupCommand(), List.of("surrender"),
                "Does structure-related stuff.", Commands.structureCommand(), List.of(),
                "For various debugging purposes.", Commands.debugCommand(), List.of("testing")
        ));
    }

    @Override
    public void onDisable() {
        GameManager.GAMES.forEach(game -> game.draw(true));
        Bukkit.unloadWorld("world", false); // Prevent saving of the world!
        DATABASE.closeConnection();
    }

    void translations() {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI("https://marcpg.com/neobb/lang/all")).GET().build();
            String response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
            Translation.loadMaps(new Gson().fromJson(response, new TypeToken<Map<Locale, Map<String, String>>>(){}.getType()));
        } catch (Exception e) {
            LOG.error("Could not retrieve translations from https://marcpg.com/neobb/lang/all.", e);
        }
    }

    void connectDatabase() {
        if (Configuration.DATABASE_ENABLED) {
            try {
                DATABASE = new AutoCatchingSQLConnection<>(
                        SQLConnection.DatabaseType.POSTGRESQL,
                        Objects.requireNonNull(Configuration.CONFIG.getString("database.address")),
                        Configuration.CONFIG.getInt("database.port", 0),
                        Configuration.CONFIG.getString("database.database", "hectus"),
                        Configuration.CONFIG.getString("database.user"),
                        Configuration.CONFIG.getString("database.passwd"),
                        Configuration.CONFIG.getString("database.table", "neobb_playerdata"),
                        "uuid",
                        e -> LOG.error("Database error.", e)
                );
            } catch (Exception e) {
                LOG.error("Could not establish connection to database! Using a dummy database instead.", e);
                DATABASE = new DummySQLConnection<>("table", "uuid");
            }
        }
    }
}
