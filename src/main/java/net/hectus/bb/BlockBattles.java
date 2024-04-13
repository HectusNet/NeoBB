package net.hectus.bb;

import com.marcpg.libpg.data.database.sql.AutoCatchingSQLConnection;
import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.command.ChallengeCommand;
import net.hectus.bb.command.GiveupCommand;
import net.hectus.bb.structure.StructureCommand;
import net.hectus.bb.structure.StructureManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public final class BlockBattles extends JavaPlugin {
    public static Logger LOG;
    public static Path DATA_DIR;
    public static boolean globalStructures;
    public static final AutoCatchingSQLConnection<UUID> DATABASE = null;

    // TODO: static {
    //     try {
    //         DATABASE = new AutoCatchingSQLConnection<>(SQLConnection.DatabaseType.POSTGRESQL, "localhost", 0, "hectus", "blockbattles", "password123", "blockbattles", "uuid",
    //                 e -> LOG.error("There was an issue while interacting with the database: {}", e.getMessage()));
    //     } catch (SQLException | ClassNotFoundException e) {
    //         throw new RuntimeException(e);
    //     }
    // }

    @Override
    public void onEnable() {
        LOG = getSLF4JLogger();
        DATA_DIR = getDataFolder().toPath();

        saveDefaultConfig();
        if (StructureManager.STRUCTURE_DIR.mkdirs()) {
            LOG.info("Created BlockBattles/structures directory, as it didn't exist before!");
        }
        if (DATA_DIR.resolve("lang").toFile().mkdirs()) {
            LOG.info("Created BlockBattles/lang directory, as it didn't exist before!");
        }
        StructureManager.STRUCTURE_DIR = getConfig().getBoolean("global-structures") ?
                new File("~/.config/neobb-structures") :
                BlockBattles.DATA_DIR.resolve("structures").toFile();

        Objects.requireNonNull(getCommand("challenge")).setExecutor(new ChallengeCommand());
        // TODO: Objects.requireNonNull(getCommand("game")).setExecutor(new Command());
        Objects.requireNonNull(getCommand("structure")).setExecutor(new StructureCommand());
        Objects.requireNonNull(getCommand("giveup")).setExecutor(new GiveupCommand());

        try {
            translations();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StructureManager.load();

        LOG.info("Successfully started up BlockBattles's plugin!");
    }

    @Override
    public void onDisable() {
        // TODO: DATABASE.closeConnection();
        StructureManager.save();
        LOG.info("Successfully shut down all components of BlockBattles's plugin!");
    }

    void translations() throws IOException {
        Files.copy(Objects.requireNonNull(getResource("en_US.yml")), DATA_DIR.resolve("lang/en_US.yml"), StandardCopyOption.REPLACE_EXISTING);
        Map<String, String> en_US = YamlConfiguration.loadConfiguration(DATA_DIR.resolve("lang/en_US.yml").toFile()).getValues(true)
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> String.valueOf(o.getValue())));
        Translation.loadSingleMap(new Locale("en", "US"), en_US);
    }
}
