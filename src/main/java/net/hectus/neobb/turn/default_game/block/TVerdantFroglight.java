package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TVerdantFroglight extends BlockTurn implements CounterattackFunction, WaterClazz {
    public TVerdantFroglight(NeoPlayer player) { super(player); }
    public TVerdantFroglight(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 2;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(SupernaturalClazz.class));
    }
}
