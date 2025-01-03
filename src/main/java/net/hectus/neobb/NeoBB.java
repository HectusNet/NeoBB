package net.hectus.neobb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marcpg.libpg.data.database.sql.AutoCatchingSQLConnection;
import com.marcpg.libpg.data.database.sql.DummySQLConnection;
import com.marcpg.libpg.data.database.sql.SQLConnection;
import com.marcpg.libpg.lang.Translation;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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

        PRODUCTION = CONFIG.getBoolean("production");

        try {
            translations();
        } catch (Exception e) {
            LOG.error("Could not load translations: {}", e.getMessage());
        }
        connectDatabase();
        StructureManager.load();

        InvUI.getInstance().setPlugin(this);

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(Commands.startCommand());
            event.registrar().register(Commands.giveupCommand());
            event.registrar().register(Commands.structureCommand());
            event.registrar().register(Commands.debugCommand());
        });

        getServer().getPluginManager().registerEvents(new GameEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new TurnEvents(), this);
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
