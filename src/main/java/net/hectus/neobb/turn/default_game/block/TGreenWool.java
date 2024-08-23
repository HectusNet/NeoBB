package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TGreenWool extends BlockTurn implements CounterattackFunction, NatureClazz {
    public TGreenWool(NeoPlayer player) { super(player); }
    public TGreenWool(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NatureClazz.class), CounterFilter.clazz(HotClazz.class), CounterFilter.clazz(ColdClazz.class));
    }
}
