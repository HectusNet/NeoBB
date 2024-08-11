package net.hectus.neobb.event;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.game.util.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameEvents implements Listener {
    @EventHandler
    public void onServerTickEnd(ServerTickEndEvent event) {
        GameManager.GAMES.forEach(Game::gameTick);
    }
}
