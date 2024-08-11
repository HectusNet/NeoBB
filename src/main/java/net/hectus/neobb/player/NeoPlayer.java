package net.hectus.neobb.player;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.hectus.neobb.game.BossBarGame;
import net.hectus.neobb.game.Game;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NeoPlayer implements Target, ForwardingAudience.Single {
    private static final ScoreboardManager SCOREBOARD_MANAGER = Bukkit.getScoreboardManager();

    public final Player player;
    public final Game game;
    public final NeoInventory inventory;

    private final Set<String> modifiers = new HashSet<>();
    private int luck;

    public NeoPlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.inventory = new NeoInventory(this);
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

    public List<NeoPlayer> opponents() {
        return game.players().stream()
                .filter(p -> p != this)
                .toList();
    }

    public TargetObj opponentTarget() {
        return new TargetObj(opponents());
    }

    @Override
    public @NotNull Audience audience() {
        return player;
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
}
