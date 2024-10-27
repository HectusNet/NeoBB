package net.hectus.neobb.game;

import com.marcpg.libpg.data.time.Time;
import net.hectus.neobb.game.util.GameInfo;
import net.hectus.neobb.lore.DefaultItemLoreBuilder;
import net.hectus.neobb.shop.DummyShop;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DummyGame extends Game {
    public static final GameInfo INFO = new GameInfo(false, 1.0, 1, 1, new Time(1), 1, DummyShop.class, DefaultItemLoreBuilder.class, List.of());

    public DummyGame(@NotNull Player player) {
        super(player.getWorld(), player);
    }

    @Override
    public GameInfo info() {
        return INFO;
    }
}
