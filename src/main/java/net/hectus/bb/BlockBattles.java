package net.hectus.bb;

import com.marcpg.libpg.data.database.sql.AutoCatchingSQLConnection;
import com.marcpg.libpg.data.database.sql.SQLConnection;
import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.structure.StructureManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;

public final class BlockBattles extends JavaPlugin {
    public static Logger LOG;
    public static Plugin PLUGIN;
    public static Path DATA_DIR;
    public static final AutoCatchingSQLConnection<UUID> DATABASE;

    static {
        try {
            DATABASE = new AutoCatchingSQLConnection<>(SQLConnection.DatabaseType.POSTGRESQL, "localhost", 0, "hectus", "blockbattles", "password123", "blockbattles", "uuid",
                    e -> LOG.error("There was an issue while interacting with the database: " + e.getMessage()));
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {
        LOG = getSLF4JLogger();
        PLUGIN = this;
        DATA_DIR = getDataFolder().toPath();

        saveDefaultConfig();

        // TODO: Objects.requireNonNull(getCommand("challenge")).setExecutor(new Command());
        // TODO: Objects.requireNonNull(getCommand("game")).setExecutor(new Command());
        // TODO: Objects.requireNonNull(getCommand("structure")).setExecutor(new Command());
        // TODO: Objects.requireNonNull(getCommand("giveup")).setExecutor(new Command());

        try {
            Translation.loadProperties(DATA_DIR.resolve("lang").toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LOG.info("Successfully started up BlockBattles's plugin!");
    }

    @Override
    public void onDisable() {
        DATABASE.closeConnection();
        LOG.info("Successfully shut down all components of BlockBattles's plugin!");
    }
}
