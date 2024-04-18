package net.hectus.bb.turn.buff;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.bb.player.PlayerData;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ChanceBuff extends Buff {
    private final Buff buff;
    private final int chance; // In Percentage (P%)

    public ChanceBuff(@NotNull Buff buff, int chance) {
        super(buff.target);
        this.buff = buff;
        this.chance = chance;
    }

    @Override
    public void apply(PlayerData player) {
        if (Randomizer.boolByChance(chance))
            buff.apply(player);
    }

    @Override
    public void revert(PlayerData player) {
        buff.revert(player);
    }

    @Override
    public String text(Locale l) {
        return chance + "% | " + buff.text(l);
    }

    @Override
    public Result result() {
        return buff.result();
    }
}
