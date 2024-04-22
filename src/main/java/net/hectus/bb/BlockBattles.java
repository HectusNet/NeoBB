package net.hectus.bb;

import com.marcpg.libpg.data.database.sql.AutoCatchingSQLConnection;
import com.marcpg.libpg.data.database.sql.SQLConnection;
import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.command.ChallengeCommand;
import net.hectus.bb.command.GiveupCommand;
import net.hectus.bb.command.InitializeBBCommand;
import net.hectus.bb.event.GameEvents;
import net.hectus.bb.event.PlayerEvents;
import net.hectus.bb.game.util.TurnScheduler;
import net.hectus.bb.structure.StructureCommand;
import net.hectus.bb.structure.StructureManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public final class BlockBattles extends JavaPlugin {
    public static  final String VERSION = "NeoBB-0.1.0 (d" + Integer.toHexString(LocalDateTime.now().getDayOfYear()) + "h" + Integer.toHexString(LocalDateTime.now().getHour()) + ")";
    public static Logger LOG;
    public static Path DATA_DIR;
    public static boolean GLOBAL_STRUCTURES;
    public static AutoCatchingSQLConnection<UUID> DATABASE = null;

    @Override
    public void onEnable() {
        LOG = getSLF4JLogger();
        DATA_DIR = getDataFolder().toPath();

        saveDefaultConfig();
        GLOBAL_STRUCTURES = getConfig().getBoolean("global-structures");

        // Create the plugin directory tree:
        if (StructureManager.STRUCTURE_DIR.mkdirs())
            LOG.info("Created BlockBattles/structures directory, as it didn't exist before!");
        if (DATA_DIR.resolve("lang").toFile().mkdirs())
            LOG.info("Created BlockBattles/lang directory, as it didn't exist before!");

        Objects.requireNonNull(getCommand("challenge")).setExecutor(new ChallengeCommand());
        Objects.requireNonNull(getCommand("structure")).setExecutor(new StructureCommand());
        Objects.requireNonNull(getCommand("giveup")).setExecutor(new GiveupCommand());
        Objects.requireNonNull(getCommand("initialize-bb")).setExecutor(new InitializeBBCommand());

        getServer().getPluginManager().registerEvents(new GameEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new TurnScheduler(), this);

        try {
            translations();
            connectDatabase();
            StructureManager.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LOG.info("Successfully started up BlockBattles' plugin!");
    }

    @Override
    public void onDisable() {
        if (DATABASE != null) DATABASE.closeConnection();
        StructureManager.save();
        LOG.info("Successfully shut down all components of BlockBattles' plugin!");
    }

    void translations() throws IOException {
        Files.copy(Objects.requireNonNull(getResource("en_US.yml")), DATA_DIR.resolve("lang/en_US.yml"), StandardCopyOption.REPLACE_EXISTING);
        for (File file : Objects.requireNonNull(DATA_DIR.resolve("lang").toFile().listFiles())) {
            String[] lang = file.getName().replace(".yml", "").split("_");
            Translation.loadSingleMap(new Locale(lang[0], lang[1]), YamlConfiguration.loadConfiguration(file).getValues(true)
                    .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> String.valueOf(o.getValue()))));
        }
    }

    void connectDatabase() {
        ConfigurationSection db = Objects.requireNonNull(getConfig().getConfigurationSection("database"));
        if (db.getBoolean("enabled")) {
            try {
                DATABASE = new AutoCatchingSQLConnection<>(
                        SQLConnection.DatabaseType.POSTGRESQL,
                        Objects.requireNonNull(db.getString("address")),
                        db.getString("username"),
                        db.getString("password"),
                        db.getString("table"),
                        "uuid",
                        e -> LOG.error("There was an issue while interacting with the database: {}", e.getMessage()));
            } catch (SQLException | ClassNotFoundException e) {
                LOG.error("Couldn't establish connection to playerdata database!");
            }
        }
    }
}
