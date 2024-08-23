package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import org.bukkit.block.Block;

import java.util.List;

public class THayBlock extends BlockTurn implements CounterattackFunction, NatureClazz {
    public THayBlock(NeoPlayer player) { super(player); }
    public THayBlock(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(ColdClazz.class), CounterFilter.clazz(WaterClazz.class));
    }
}
