package net.hectus.neobb.turn.default_game.throwable;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TSnowball extends ThrowableTurn implements CounterbuffFunction, ColdClazz {
    public TSnowball(NeoPlayer player) { super(player); }
    public TSnowball(Projectile data, Location impact, NeoPlayer player) { super(data, impact, player); }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn(), new Buff.Luck(Buff.BuffTarget.YOU, 15), new Buff.Effect(Buff.BuffTarget.OPPONENTS, PotionEffectType.GLOWING));
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(HotClazz.class));
    }
}
