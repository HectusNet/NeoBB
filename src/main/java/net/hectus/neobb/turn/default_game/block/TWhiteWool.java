package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TWhiteWool extends BlockTurn implements CounterbuffFunction, ColdClazz {
    public TWhiteWool(NeoPlayer player) { super(player); }
    public TWhiteWool(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.ALL, PotionEffectType.SLOWNESS, 9));
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(WaterClazz.class), CounterFilter.clazz(RedstoneClazz.class), CounterFilter.clazz(SupernaturalClazz.class));
    }
}
