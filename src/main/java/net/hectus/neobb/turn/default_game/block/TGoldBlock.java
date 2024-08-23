package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TGoldBlock extends BlockTurn implements CounterattackFunction, NeutralClazz {
    public TGoldBlock(NeoPlayer player) { super(player); }
    public TGoldBlock(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(HotClazz.class), CounterFilter.clazz(NatureClazz.class));
    }
}
