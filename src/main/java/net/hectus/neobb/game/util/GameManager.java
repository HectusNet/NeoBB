package net.hectus.neobb.game.util;

import net.hectus.neobb.game.Game;
import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GameManager {
    public static final List<Game> GAMES = new ArrayList<>();

    public static @Nullable Game game(String id) {
        for (Game game : GAMES) {
            if (game.id.equals(id)) return game;
        }
        return null;
    }

    public static @Nullable Game game(Collection<String> scoreboardIDs) {
        for (Game game : GAMES) {
            if (scoreboardIDs.contains(game.id)) return game;
        }
        return null;
    }

    public static @Nullable Game game(Player player, boolean onlyAlive) {
        for (Game game : GAMES) {
            if (game.player(player, onlyAlive) != null) return game;
        }
        return null;
    }

    public static @Nullable NeoPlayer player(Player player, boolean onlyAlive) {
        for (Game game : GAMES) {
            NeoPlayer p = game.player(player, onlyAlive);
            if (p != null) return p;
        }
        return null;
    }
}
