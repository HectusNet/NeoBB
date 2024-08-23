package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TSculk extends BlockTurn implements CounterbuffFunction, NeutralClazz {
    public TSculk(NeoPlayer player) { super(player); }
    public TSculk(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.OPPONENTS, 10), new Buff.Effect(Buff.BuffTarget.OPPONENTS, PotionEffectType.DARKNESS));
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(SupernaturalClazz.class));
    }
}
