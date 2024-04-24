package net.hectus.bb.player;

import com.marcpg.libpg.storing.Pair;
import net.hectus.bb.game.Game;
import net.hectus.bb.turn.effective.Attack;
import net.hectus.bb.turn.effective.Defense;
import net.hectus.bb.turn.effective.TurnCounter;
import net.hectus.bb.util.Modifiers;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class PlayerData {
    public final Modifiers modifiers = new Modifiers();

    private final Player player;
    private final Game game;

    private final List<TurnCounter> specialEffects = new ArrayList<>();
    private final PlayerInv inventory = new PlayerInv(this);

    private boolean attacked;
    private Attack attack;

    private boolean defensive;
    private Defense defense;

    private int luck = 20;
    private int extraTurns = 0;

    public PlayerData(Player player, Game game) {
        this.player = player;
        this.game = game;
        player.showBossBar(bossBar);
    }

    public Player player() {
        return player;
    }

    public PlayerData opponent() {
        return game.getOpponent(this);
    }

    public Game game() {
        return game;
    }

    public @NotNull Pair<Boolean, @Nullable Attack> getAttack() {
        return Pair.of(attacked, attack);
    }

    public void setAttack(@NotNull Pair<Boolean, @Nullable Attack> attack) {
        this.attacked = attack.left();
        this.attack = attack.right();
    }

    public @NotNull Pair<Boolean, @Nullable Defense> getDefense() {
        return Pair.of(defensive, defense);
    }

    public void setDefense(@NotNull Pair<Boolean, @Nullable Defense> defense) {
        this.defensive = defense.left();
        this.defense = defense.right();
    }

    public List<TurnCounter> specialEffects() {
        return specialEffects;
    }

    public PlayerInv inv() {
        return inventory;
    }

    public boolean extraTurn() {
        return --extraTurns > 0;
    }

    public void addExtraTurns(int amount) {
        extraTurns = Math.max(amount, extraTurns + amount);
    }

    public int luck() {
        return luck;
    }

    public void addLuck(int amount) {
        luck += amount;
    }

    public void removeLuck(int amount) {
        luck -= amount;
    }

    // ========== BOSSBAR ==========

    private final BossBar bossBar = BossBar.bossBar(Component.empty(), 0.0f, BossBar.Color.RED, BossBar.Overlay.NOTCHED_10);

    public void updateBossBar() {
        bossBar.name(Component.text(game.ticker().turnCountdown() + "s"));
        bossBar.progress(game.ticker().turnCountdown() * 0.1f);
        bossBar.color(getColor());
    }

    private BossBar.Color getColor() {
        return switch (game.ticker().turnCountdown()) {
            case 10, 9, 8 -> BossBar.Color.WHITE;
            case 7, 6 -> BossBar.Color.BLUE;
            case 5, 4 -> BossBar.Color.GREEN;
            case 3, 2 -> BossBar.Color.YELLOW;
            case 1 -> BossBar.Color.RED;
            default -> BossBar.Color.PINK;
        };
    }
}
