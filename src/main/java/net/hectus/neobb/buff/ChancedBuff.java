package net.hectus.neobb.buff;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.player.NeoPlayer;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ChancedBuff extends Buff {
    private final Buff buff;
    private final double chance;

    public ChancedBuff(@NotNull Buff buff, double chance) {
        super(buff.buffTarget);
        this.buff = buff;
        this.chance = chance;
    }

    @Override
    public void apply(NeoPlayer source) {
        if (Randomizer.boolByChance(chance))
            buff.apply(source);
    }

    @Override
    public String text(Locale l) {
        return chance + "% | " + buff.text(l);
    }

    @Override
    public TextColor color() {
        return buff.color();
    }
}
