package net.hectus.bb.player;

import com.marcpg.libpg.storing.Pair;
import net.hectus.bb.game.Game;
import net.hectus.bb.turn.effective.Attack;
import net.hectus.bb.turn.effective.Defense;
import net.hectus.bb.turn.effective.TurnCounter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    private final Player player;
    private final Game game;

    private boolean attacked;
    private Attack attack;

    private boolean defensive;
    private Defense defense;

    private final List<TurnCounter> specialEffects = new ArrayList<>();
    private final PlayerInv inventory = new PlayerInv(this);

    private int luck = 20;
    private int extraTurns = 0;

    public PlayerData(Player player, Game game) {
        this.player = player;
        this.game = game;
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

    public Pair<Boolean, @Nullable Attack> getAttack() {
        return Pair.of(attacked, attack);
    }

    public void setAttack(@NotNull Pair<Boolean, @Nullable Attack> attack) {
        this.attacked = attack.left();
        this.attack = attack.right();
    }

    public Pair<Boolean, @Nullable Defense> getDefense() {
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

    /**
     * Does all logic that should apply after each turn.
     * @return true if the player has an extra turn to use, false otherwise.
     */
    public boolean postTurn() {
        return --extraTurns <= 0;
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
}
