package net.hectus.neobb.game;

import net.hectus.neobb.util.Cord;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BossBarGame extends Game {
    protected BossBar bossBar;

    public BossBarGame(World world, @NotNull List<Player> players, Cord corner1, Cord corner2) {
        super(world, players, corner1, corner2);
    }

    public abstract BossBar initialBossBar();
    public abstract void bossBar(BossBar bossBar);

    public BossBar bossBar() {
        return bossBar;
    }

    @Override
    public void start() {
        super.start();
        bossBar = initialBossBar();
        bossBar.addViewer(gameTarget(false));
    }

    @Override
    public void end() {
        bossBar.removeViewer(gameTarget(false));
        super.end();
    }
}
