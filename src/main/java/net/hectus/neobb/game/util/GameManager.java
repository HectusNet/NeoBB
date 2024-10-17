package net.hectus.neobb.game.util;

import net.hectus.neobb.game.Game;
import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class GameManager {
    public static final List<Game> GAMES = new ArrayList<>();

    private static final Map<String, Game> gameCacheById = new HashMap<>();
    private static final Map<UUID, NeoPlayer> playerCache = new HashMap<>();

    public static void add(Game game) {
        GAMES.add(game);
        gameCacheById.put(game.id, game);
    }

    public static void remove(Game game) {
        GAMES.remove(game);
        gameCacheById.remove(game.id);
        game.initialPlayers().forEach(p -> playerCache.remove(p.uuid()));
    }

    public static @Nullable Game game(String id) {
        if (id == null) return null;
        return gameCacheById.get(id);
    }

    public static @Nullable Game game(Collection<String> scoreboardIDs) {
        for (Game game : GAMES) {
            if (scoreboardIDs.contains(game.id)) return game;
        }
        return null;
    }

    public static @Nullable NeoPlayer player(@NotNull Player player, boolean onlyAlive) {
        NeoPlayer cached = playerCache.get(player.getUniqueId());
        if (cached != null && (!onlyAlive || cached.game.players().contains(cached)))
            return cached;

        for (Game game : GAMES) { // Fallback if not cached.
            NeoPlayer p = game.player(player, onlyAlive);
            if (p != null) {
                playerCache.put(player.getUniqueId(), p);
                return p;
            }
        }
        return null;
    }
}
