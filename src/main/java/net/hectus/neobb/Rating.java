package net.hectus.neobb;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Utilities;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class Rating {
    private static final double K = 32.0; // General Factor

    private static final double WIN_FACTOR = 1.0;
    private static final double DRAW_FACTOR = 0.5;
    private static final double LOSS_FACTOR = 0.0;

    public static void updateRankings(@NotNull List<NeoPlayer> winners, @NotNull List<NeoPlayer> losers) {
        double averageEloWinners = avgElo(winners);
        double averageEloLosers = avgElo(losers);

        for (NeoPlayer winner : winners) {
            winner.setElo((int) (winner.elo() + calculateKFactor(winner) * (WIN_FACTOR - 1.0 / (1 + Math.pow(10, (averageEloLosers - winner.elo()) / 400.0)))));
            winner.addWin();
        }
        for (NeoPlayer loser : losers) {
            loser.setElo((int) (loser.elo() + calculateKFactor(loser) * (LOSS_FACTOR - 1.0 / (1 + Math.pow(10, (averageEloWinners - loser.elo()) / 400.0)))));
            loser.addLoss();
        }
    }

    public static void updateRankings(@NotNull List<NeoPlayer> players) {
        double averageElo = avgElo(players);
        for (NeoPlayer player : players) {
            player.setElo((int) (player.elo() + calculateKFactor(player) * (DRAW_FACTOR - 1.0 / (1 + Math.pow(10, (averageElo - player.elo()) / 400.0)))));
            player.addDraw();
        }
    }

    private static double calculateKFactor(@NotNull NeoPlayer player) {
        return K / Math.sqrt(1 + player.gamesPlayed() / K);
    }

    private static double avgElo(@NotNull List<NeoPlayer> players) {
        return players.stream().mapToDouble(NeoPlayer::elo).average().orElse(0);
    }

    public static void sendInfo(Audience receiver, @NotNull UUID player) {
        if (NeoBB.DATABASE.contains(player))
            NeoBB.DATABASE.add(Map.of("uuid", player));

        Map<String, Object> data = NeoBB.DATABASE.getRowMap(player);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getKey().equals("uuid")) continue;
            receiver.sendMessage(Component.text(Utilities.capitalizeFirstLetter(entry.getKey()) + ": ", Colors.BLUE).append(Component.text(entry.getValue().toString())));
        }
    }
}
