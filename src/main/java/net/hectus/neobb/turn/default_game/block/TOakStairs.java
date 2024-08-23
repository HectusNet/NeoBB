package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TOakStairs extends BlockTurn implements CounterbuffFunction, NatureClazz {
    public TOakStairs(NeoPlayer player) { super(player); }
    public TOakStairs(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn());
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(HotClazz.class), CounterFilter.clazz(NatureClazz.class), CounterFilter.clazz(SupernaturalClazz.class));
    }
}
