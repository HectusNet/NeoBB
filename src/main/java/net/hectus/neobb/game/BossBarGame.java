package net.hectus.neobb.game;

import net.hectus.neobb.turn.default_game.warp.WarpTurn;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BossBarGame extends Game {
    protected BossBar bossBar;

    public BossBarGame(boolean ranked, World world, @NotNull List<Player> players, WarpTurn defaultWarp) {
        super(ranked, world, players, defaultWarp);
    }

    public BossBar initialBossBar() {
        return BossBar.bossBar(Component.text("Turn Countdown: -"), 0.0f, BossBar.Color.YELLOW, BossBar.Overlay.NOTCHED_10);
    }

    public void bossBar(@NotNull BossBar bossBar) {
        bossBar.name(Component.text("Turn Countdown: " + turnCountdown + "s"));
        bossBar.progress(Math.clamp((float) turnCountdown / info().turnTimer(), 0.0f, 1.0f));
    }

    public final BossBar bossBar() {
        return bossBar;
    }

    @Override
    public void start() {
        super.start();
        bossBar = initialBossBar();
        bossBar.addViewer(gameTarget(false));
    }

    @Override
    public void end(boolean force) {
        bossBar.removeViewer(gameTarget(false));
        super.end(force);
    }
}
