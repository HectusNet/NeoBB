package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TBlueIce extends BlockTurn implements CounterFunction, ColdClazz {
    public TBlueIce(NeoPlayer player) { super(player); }
    public TBlueIce(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(ColdClazz.class), CounterFilter.clazz(WaterClazz.class), CounterFilter.clazz(SupernaturalClazz.class));
    }
}
