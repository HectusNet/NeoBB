package net.hectus.neobb.player;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.cosmetic.PlaceParticle;
import net.hectus.neobb.game.BossBarGame;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.util.Modifiers;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class NeoPlayer extends Modifiers.Modifiable implements Target, ForwardingAudience.Single {
    private static final ScoreboardManager SCOREBOARD_MANAGER = Bukkit.getScoreboardManager();

    public final Player player;
    public final Game game;
    public NeoInventory inventory;

    private int luck = 20;
    private double health = 50;

    private PlaceParticle cosmeticParticle;
    private NamedTextColor cosmeticOutline;

    private double elo;
    private double ratingDeviation;
    private double volatility;
    private long lastMatchTimeMillis;
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
            Objective objective;

            if (player.getScoreboard() == SCOREBOARD_MANAGER.getMainScoreboard()) {
                Scoreboard sb = SCOREBOARD_MANAGER.getNewScoreboard();
                objective = sb.registerNewObjective("neobb", Criteria.DUMMY, scoreboard.getFirst());
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                player.setScoreboard(sb);
            } else {
                objective = player.getScoreboard().getObjective("neobb");
                if (objective == null)
                    objective = player.getScoreboard().registerNewObjective("neobb", Criteria.DUMMY, scoreboard.getFirst());
            }

            for (int i = scoreboard.size() - 1; i > 0; i--) {
                Score score = objective.getScore("score-" + i);
                score.numberFormat(NumberFormat.blank());
                score.setScore(scoreboard.size() - i);

                score.customName(scoreboard.get(i));
            }
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

    public int luck() {
        return luck;
    }

    public void addLuck(int luck) {
        this.luck += luck;
    }

    public double health() {
        return health;
    }

    public void damage(double damage) {
        health(health - damage);
        if (damage > 0.0) // Ensure that it's actually damage and not just weird healing.
            player.playSound(player, Sound.ENTITY_PLAYER_HURT, 0.5f, 1.0f);
    }

    public void health(double health) {
        this.health = Math.max(0.0, health);
        player.setHealth(Math.max(0.5, health / 5)); // "health / 5" is effectively the same as "health / 100 * 20".

        if (health <= 0.0) game.eliminatePlayer(this);
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

        elo = (double) NeoBB.DATABASE.get(uuid(), "elo");
        ratingDeviation = (double) NeoBB.DATABASE.get(uuid(), "rating_deviation");
        volatility = (double) NeoBB.DATABASE.get(uuid(), "volatility");
        lastMatchTimeMillis = (long) NeoBB.DATABASE.get(uuid(), "last_match_time");
        wins = (int) NeoBB.DATABASE.get(uuid(), "wins");
        losses = (int) NeoBB.DATABASE.get(uuid(), "losses");
        draws = (int) NeoBB.DATABASE.get(uuid(), "draws");
        turns = (int) NeoBB.DATABASE.get(uuid(), "turns");

        cosmeticParticle = PlaceParticle.valueOf((String) NeoBB.DATABASE.get(uuid(), "cosmetic_particle"));
        cosmeticOutline = NamedTextColor.namedColor((Integer) NeoBB.DATABASE.get(uuid(), "cosmetic_outline"));
    }

    // ====================================
    // ========== DATABASE STATS ==========
    // ====================================

    public double elo() { return elo; }
    public void setElo(double elo) {
        this.elo = elo;
        NeoBB.DATABASE.set(uuid(), "elo", elo);
    }

    public double ratingDeviation() { return ratingDeviation; }
    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
        NeoBB.DATABASE.set(uuid(), "rating_deviation", ratingDeviation);
    }

    public double volatility() { return volatility; }
    public void setVolatility(double volatility) {
        this.volatility = volatility;
        NeoBB.DATABASE.set(uuid(), "volatility", volatility);
    }

    public long lastMatchTimeMillis() { return lastMatchTimeMillis; }
    public void setLastMatchTimeMillis(long lastMatchTimeMillis) {
        this.lastMatchTimeMillis = lastMatchTimeMillis;
        NeoBB.DATABASE.set(uuid(), "last_match_time", lastMatchTimeMillis);
    }

    public void addWin() {
        wins++;
        NeoBB.DATABASE.set(uuid(), "wins", wins);
    }

    public void addLoss() {
        losses++;
        NeoBB.DATABASE.set(uuid(), "losses", losses);
    }

    public void addDraw() {
        draws++;
        NeoBB.DATABASE.set(uuid(), "draws", draws);
    }

    public void addTurn() {
        turns++;
        NeoBB.DATABASE.set(uuid(), "turns", turns);
    }

    // ====================================
    // ======== DATABASE COSMETICS ========
    // ====================================

    public PlaceParticle cosmeticParticle() { return cosmeticParticle; }
    public NamedTextColor cosmeticOutline() { return cosmeticOutline; }
}
