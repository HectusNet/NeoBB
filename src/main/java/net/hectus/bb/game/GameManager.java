package net.hectus.bb.game;

import net.hectus.bb.player.PlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class GameManager {
    public static final List<Game> GAMES = new ArrayList<>();

    public static @Nullable PlayerData getPlayerData(Player player) {
        for (Game game : GAMES) {
            for (PlayerData playerData : game.players()) {
                if (playerData.player() == player) {
                    return playerData;
                }
            }
        }
        return null;
    }

    public static @Nullable Game getGame(Player player) {
        PlayerData playerData = getPlayerData(player);
        return playerData == null ? null : playerData.game();
    }
}
