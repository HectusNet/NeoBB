package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TBrainCoralBlock extends BlockTurn implements CounterattackFunction, WaterClazz {
    public TBrainCoralBlock(NeoPlayer player) { super(player); }
    public TBrainCoralBlock(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 2;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(RedstoneClazz.class));
    }
}
