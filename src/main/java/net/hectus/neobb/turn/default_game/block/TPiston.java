package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TPiston extends BlockTurn implements CounterFunction, RedstoneClazz {
    public TPiston(NeoPlayer player) { super(player); }
    public TPiston(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(t -> true, "all"));
    }
}
