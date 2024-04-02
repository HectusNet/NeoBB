package net.hectus.bb;

import com.marcpg.libpg.storing.Pair;
import net.hectus.bb.counter.Attack;
import net.hectus.bb.counter.Defense;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerData {
    private final Player player;
    private final Match match;

    private boolean attacked;
    private Attack attack;

    private boolean defensive;
    private Defense defense;

    public PlayerData(Player player, Match match) {
        this.player = player;
        this.match = match;
    }

    public void lose() {

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
}
