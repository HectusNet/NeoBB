package net.hectus.neobb.player;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.cosmetic.PlaceParticle;
import net.hectus.neobb.game.BossBarGame;
import net.hectus.neobb.game.Game;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class NeoPlayer implements Target, ForwardingAudience.Single {
    private static final ScoreboardManager SCOREBOARD_MANAGER = Bukkit.getScoreboardManager();

    private final static String ELO_COL_NAME = "elo";
    private final static String RATING_DEVIATION_COL_NAME = "rating_deviation";
    private final static String VOLATILITY_COL_NAME = "volatility";
    private final static String LAST_MATCH_TIME_COL_NAME = "last_match_time";

    public final Player player;
    public final Game game;
    public NeoInventory inventory;

    private final Set<String> modifiers = new HashSet<>();
    private int luck = 20;

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

        elo = (double) NeoBB.DATABASE.get(uuid(), ELO_COL_NAME);
        ratingDeviation = (double) NeoBB.DATABASE.get(uuid(), RATING_DEVIATION_COL_NAME);
        volatility = (double) NeoBB.DATABASE.get(uuid(), VOLATILITY_COL_NAME);
        lastMatchTimeMillis = (long) NeoBB.DATABASE.get(uuid(), LAST_MATCH_TIME_COL_NAME);
        wins = (int) NeoBB.DATABASE.get(uuid(), "wins");
        losses = (int) NeoBB.DATABASE.get(uuid(), "losses");
        draws = (int) NeoBB.DATABASE.get(uuid(), "draws");
        turns = (int) NeoBB.DATABASE.get(uuid(), "turns");

        cosmeticParticle = PlaceParticle.valueOf((String) NeoBB.DATABASE.get(uuid(), "cosmetic_particle"));
        cosmeticOutline = NamedTextColor.namedColor((Integer) NeoBB.DATABASE.get(uuid(), "cosmetic_outline"));
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

    public double elo() { return elo; }
    public void setElo(double elo) {
        this.elo = elo;
        NeoBB.DATABASE.set(uuid(), ELO_COL_NAME, elo);
    }

    public double ratingDeviation() { return ratingDeviation; }
    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
        NeoBB.DATABASE.set(uuid(), RATING_DEVIATION_COL_NAME, ratingDeviation);
    }

    public double volatility() { return volatility; }
    public void setVolatility(double volatility) {
        this.volatility = volatility;
        NeoBB.DATABASE.set(uuid(), VOLATILITY_COL_NAME, volatility);
    }

    public long lastMatchTimeMillis() { return lastMatchTimeMillis; }
    public void setLastMatchTimeMillis(long lastMatchTimeMillis) {
        this.lastMatchTimeMillis = lastMatchTimeMillis;
        NeoBB.DATABASE.set(uuid(), LAST_MATCH_TIME_COL_NAME, lastMatchTimeMillis);
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

    // ====================================
    // ======== DATABASE COSMETICS ========
    // ====================================

    public PlaceParticle cosmeticParticle() { return cosmeticParticle; }
    public void setCosmeticParticle(@NotNull PlaceParticle cosmeticParticle) {
        this.cosmeticParticle = cosmeticParticle;
        NeoBB.DATABASE.set(uuid(), "cosmetic_particle", cosmeticParticle.toString());
    }

    public NamedTextColor cosmeticOutline() { return cosmeticOutline; }
    public void setCosmeticOutline(@NotNull NamedTextColor cosmeticOutline) {
        this.cosmeticOutline = cosmeticOutline;
        NeoBB.DATABASE.set(uuid(), "cosmetic_outline", cosmeticOutline.value());
    }
}
