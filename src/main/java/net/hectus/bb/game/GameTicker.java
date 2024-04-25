package net.hectus.bb.game;

import com.marcpg.libpg.color.McFormat;
import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.data.time.Timer;
import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.text.Formatter;
import net.hectus.bb.BlockBattles;
import net.hectus.bb.player.PlayerData;
import net.hectus.bb.turn.TurnData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class GameTicker extends Timer {
    private final Game game;
    private BukkitTask task;
    private int turnCountdown = 5;

    public GameTicker(Game game) {
        super(new Time(5, Time.Unit.MINUTES));
        this.game = game;
    }

    @Override
    public void start() {
        task = Bukkit.getScheduler().runTaskTimer(BlockBattles.getPlugin(BlockBattles.class), () -> {
            game.players().forEach(this::scoreboard);
            timer.decrement();
            if (timer.get() == 0) {
                game.draw();
                return;
            }

            turnCountdown--;
            game.players().forEach(PlayerData::updateBossBar);
            if (turnCountdown <= 0) {
                game.done(new TurnData(game.turning(), null, null, null));
                game.getOpponent(game.turning()).player().sendActionBar(Translation.component(game.turning().player().locale(), "gameplay.info.too-slow.turning").color(NamedTextColor.GREEN));
                game.turning().player().sendActionBar(Translation.component(game.getOpponent(game.turning()).player().locale(), "gameplay.info.too-slow.opponent").color(NamedTextColor.RED));
                turnCountdown = 5; // Reset the timer!
            }
        }, 0, 20);
    }

    @Override
    public void stop() {
        task.cancel();
    }

    public int turnCountdown() {
        return turnCountdown;
    }

    public void setTurnCountdown(int turnCountdown) {
        this.turnCountdown = turnCountdown;
    }

    // ========== SCOREBOARD ==========

    private static final ScoreboardManager SCOREBOARD_MANAGER = Bukkit.getScoreboardManager();
    private static final Component SCOREBOARD_TITLE = MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha");

    public void scoreboard(@NotNull PlayerData player) {
        Scoreboard scoreboard = SCOREBOARD_MANAGER.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("neobb", Criteria.DUMMY, SCOREBOARD_TITLE);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Locale l = player.player().locale();

        objective.getScore("   ").setScore(8);
        objective.getScore(Translation.string(l, "scoreboard.turning") + (game.turning() == player ? McFormat.GREEN + "You" : McFormat.YELLOW + game.turning().player().getName())).setScore(7);
        objective.getScore(Translation.string(l, "scoreboard.time") + timer.getPreciselyFormatted()).setScore(7);
        objective.getScore(Translation.string(l, "scoreboard.warp") + game.warp().temperature.color() + Formatter.toPascalCase(game.warp().name())).setScore(6);
        objective.getScore("  ").setScore(5);
        objective.getScore(Translation.string(l, "scoreboard.attacked") + yesNo(l, player.getAttack().left(), false)).setScore(4);
        objective.getScore(Translation.string(l, "scoreboard.defended") + yesNo(l, player.getDefense().left(), true)).setScore(3);
        objective.getScore(Translation.string(l, "scoreboard.luck") + luck(player.luck())).setScore(2);
        objective.getScore(" ").setScore(1);
        objective.getScore(McFormat.DARK_GRAY + BlockBattles.VERSION).setScore(0);

        player.player().setScoreboard(scoreboard);
    }

    public String luck(int luck) {
        return (luck < 20 ? McFormat.RED : (luck > 20 ? McFormat.GREEN : McFormat.YELLOW)).chatColor() + luck;
    }

    public String yesNo(Locale l, boolean bool, boolean inverted) {
        return (bool ^ inverted ? McFormat.RED : McFormat.GREEN) + Translation.string(l, "scoreboard." + (bool ? "yes" : "no"));
    }
}
