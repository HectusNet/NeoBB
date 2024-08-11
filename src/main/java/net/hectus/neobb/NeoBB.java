package net.hectus.neobb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marcpg.libpg.data.database.sql.AutoCatchingSQLConnection;
import com.marcpg.libpg.data.database.sql.SQLConnection;
import com.marcpg.libpg.lang.Translation;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.hectus.neobb.event.GameEvents;
import net.hectus.neobb.event.TurnEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

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
    public static final String VERSION = "0.0.1";

    public static NeoBB PLUGIN;
    public static Logger LOG;
    public static Path DATA_DIR;
    public static FileConfiguration CONFIG;
    public static AutoCatchingSQLConnection<UUID> DATABASE;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        PLUGIN = this;
        LOG = getSLF4JLogger();
        DATA_DIR = getDataFolder().toPath();
        CONFIG = getConfig();

        try {
            Class.forName("com.marcpg.libpg.LibPG");
            Class.forName("com.github.stefvanschie.inventoryframework.gui.type.util.InventoryBased");
        } catch (ClassNotFoundException e) {
            LOG.error("Could not find all dependencies: {}", e.getMessage());
        }

        try {
            translations();
        } catch (Exception e) {
            LOG.error("Could not load translations: {}", e.getMessage());
        }
        connectDatabase();

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(Commands.startCommand());
            event.registrar().register(Commands.giveupCommand());
        });

        getServer().getPluginManager().registerEvents(new TurnEvents(), this);
        getServer().getPluginManager().registerEvents(new GameEvents(), this);

        try {
            Files.createDirectories(CONFIG.getBoolean("global-structures") ? DATA_DIR.resolve("structures") : new File("~/.config/neobb-structures").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        if (DATABASE != null) DATABASE.closeConnection();
    }

    void translations() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(new URI("https://marcpg.com/neobb/lang/all")).GET().build();
        String response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        Translation.loadMaps(new Gson().fromJson(response, new TypeToken<Map<Locale, Map<String, String>>>(){}.getType()));
    }

    void connectDatabase() {
        if (CONFIG.getBoolean("database.enabled")) {
            try {
                DATABASE = new AutoCatchingSQLConnection<>(
                        SQLConnection.DatabaseType.POSTGRESQL,
                        Objects.requireNonNull(CONFIG.getString("database.address")),
                        CONFIG.getInt("database.port"),
                        CONFIG.getString("database.database"),
                        CONFIG.getString("database.username"),
                        CONFIG.getString("database.password"),
                        CONFIG.getString("database.table"),
                        "uuid",
                        e -> LOG.error("Database error: {}", e.getMessage())
                );
            } catch (Exception e) {
                LOG.error("Couldn't establish connection to playerdata database!", e);
            }
        }
    }
}
