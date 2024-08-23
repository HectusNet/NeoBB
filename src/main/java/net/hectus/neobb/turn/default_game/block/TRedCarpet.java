package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.buff.ChancedBuff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TRedCarpet extends BlockTurn implements CounterbuffFunction, RedstoneClazz {
    public TRedCarpet(NeoPlayer player) { super(player); }
    public TRedCarpet(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn(), new ChancedBuff(new Buff.Give(Buff.BuffTarget.YOU, new TRedBed(player)), 10));
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(ColdClazz.class), CounterFilter.clazz(RedstoneClazz.class));
    }
}
