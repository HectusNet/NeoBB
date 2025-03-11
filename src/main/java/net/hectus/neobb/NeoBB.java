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
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import xyz.xenondevs.invui.InvUI;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SuppressWarnings("UnstableApiUsage")
public final class NeoBB extends JavaPlugin {
    public static final String VERSION = "0.0.9";

    public static NeoBB PLUGIN;
    public static Logger LOG;
    public static Path DATA_DIR;
    public static FileConfiguration CONFIG;
    public static AutoCatchingSQLConnection<UUID> DATABASE;
    public static Path STRUCTURE_DIR;
    public static boolean PRODUCTION;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PLUGIN = this;
        LOG = getSLF4JLogger();
        DATA_DIR = getDataPath();
        CONFIG = getConfig();

        try {
            if (Objects.requireNonNullElse(CONFIG.getString("structure-mode"), "local").equals("global")) {
                STRUCTURE_DIR = new File("~/.config/neobb-structures").toPath();
            } else {
                STRUCTURE_DIR = DATA_DIR.resolve("structures");
            }
            Files.createDirectories(STRUCTURE_DIR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MinecraftLibPG.init(this);
        InvUI.getInstance().setPlugin(this);
        StructureManager.load();

        PRODUCTION = CONFIG.getBoolean("production");

        try {
            translations();
        } catch (Exception e) {
            LOG.error("Could not load translations: {}", e.getMessage());
        }
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

    void translations() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(new URI("https://marcpg.com/neobb/lang/all")).GET().build();
        String response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        Translation.loadMaps(new Gson().fromJson(response, new TypeToken<Map<Locale, Map<String, String>>>(){}.getType()));
    }

    void connectDatabase() {
        if (CONFIG.getBoolean("database.enabled")) {
            try {
                Class.forName("org.postgresql.Driver"); // Make sure the PostgreSQL driver is loaded.
                DATABASE = new AutoCatchingSQLConnection<>(
                        SQLConnection.DatabaseType.POSTGRESQL,
                        Objects.requireNonNull(CONFIG.getString("database.address")),
                        CONFIG.getInt("database.port"),
                        CONFIG.getString("database.database"),
                        CONFIG.getString("database.user"),
                        CONFIG.getString("database.passwd"),
                        CONFIG.getString("database.table"),
                        "uuid",
                        e -> LOG.error("Database error: {}", e.getMessage())
                );
                return;
            } catch (Exception e) {
                LOG.error("Couldn't establish connection to playerdata database! Using a dummy database now.", e);
            }
        }
        DATABASE = new DummySQLConnection<>("table", "uuid");
    }
}
