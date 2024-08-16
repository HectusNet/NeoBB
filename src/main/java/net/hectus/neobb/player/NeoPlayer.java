package net.hectus.neobb.player;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.BossBarGame;
import net.hectus.neobb.game.Game;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class NeoPlayer implements Target, ForwardingAudience.Single {
    private static final ScoreboardManager SCOREBOARD_MANAGER = Bukkit.getScoreboardManager();

    public final Player player;
    public final Game game;
    public final NeoInventory inventory;

    private final Set<String> modifiers = new HashSet<>();
    private int luck = 20;

    private int elo;
    private int wins;
    private int losses;
    private int draws;
    private int turns;

    public NeoPlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.inventory = new NeoInventory(this);
        fetchDatabase();
    }

    public void tick() {
        List<Component> scoreboard = game.scoreboard(this);
        if (scoreboard != null && !scoreboard.isEmpty()) {
            Scoreboard sb = SCOREBOARD_MANAGER.getNewScoreboard();

            Objective objective = sb.registerNewObjective("neobb", Criteria.DUMMY, scoreboard.getFirst());
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            for (int i = scoreboard.size() - 1; i > 0; i--) {
                Score score = objective.getScore("score-" + i);
                score.numberFormat(NumberFormat.blank());
                score.customName(scoreboard.get(i));
                score.setScore(scoreboard.size() - i);
            }
            player.setScoreboard(sb);
        }

        Component actionbar = game.actionbar(this);
        if (actionbar != null && !actionbar.equals(Component.empty()))
            player.sendActionBar(actionbar);

        if (game instanceof BossBarGame bossBarGame) {
            bossBarGame.bossBar(bossBarGame.bossBar());
        }
    }

    public List<NeoPlayer> opponents(boolean onlyAlive) {
        return (onlyAlive ? game.players() : game.initialPlayers()).stream()
                .filter(p -> p != this)
                .toList();
    }

    @Override
    public @NotNull Audience audience() {
        return player;
    }

    public NeoPlayer nextPlayer() {
        List<NeoPlayer> players = game.players();
        return players.get((players.indexOf(this) + 1) % players.size());
    }

    public final void addModifier(String modifier) {
        modifiers.add(modifier);
    }

    public final void removeModifier(String modifier) {
        modifiers.remove(modifier);
    }

    public final boolean hasModifier(String modifier) {
        return modifiers.contains(modifier);
    }

    public int luck() {
        return luck;
    }

    public Locale locale() {
        return player.locale();
    }

    public UUID uuid() {
        return player.getUniqueId();
    }

    public void fetchDatabase() {
        if (!NeoBB.DATABASE.contains(uuid())) {
            NeoBB.DATABASE.add(Map.of("uuid", uuid()));
        }
        elo = (int) NeoBB.DATABASE.get(uuid(), "elo");
        wins = (int) NeoBB.DATABASE.get(uuid(), "wins");
        losses = (int) NeoBB.DATABASE.get(uuid(), "losses");
        draws = (int) NeoBB.DATABASE.get(uuid(), "draws");
        turns = (int) NeoBB.DATABASE.get(uuid(), "turns");
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public void addLuck(int luck) {
        this.luck += luck;
    }

    public void removeLuck(int luck) {
        this.luck -= luck;
    }

    // ====================================
    // ========== DATABASE STATS ==========
    // ====================================

    public int elo() { return elo; }
    public void setElo(int elo) {
        this.elo = elo;
        NeoBB.DATABASE.set(uuid(), "elo", elo);
    }

    public int wins() { return wins; }
    public void addWin() {
        wins++;
        NeoBB.DATABASE.set(uuid(), "wins", wins);
    }

    public int losses() { return losses; }
    public void addLoss() {
        losses++;
        NeoBB.DATABASE.set(uuid(), "losses", losses);
    }

    public int draws() { return draws; }
    public void addDraw() {
        draws++;
        NeoBB.DATABASE.set(uuid(), "draws", draws);
    }

    public int turns() { return draws; }
    public void addTurn() {
        turns++;
        NeoBB.DATABASE.set(uuid(), "turns", turns);
    }

    public int gamesPlayed() { return wins + losses + draws; }
}
