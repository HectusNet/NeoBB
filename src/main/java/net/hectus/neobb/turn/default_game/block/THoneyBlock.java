package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import org.bukkit.block.Block;

import java.util.List;

public class THoneyBlock extends BlockTurn implements CounterFunction, NatureClazz {
    public THoneyBlock(NeoPlayer player) { super(player); }
    public THoneyBlock(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NatureClazz.class), CounterFilter.clazz(WaterClazz.class), CounterFilter.clazz(RedstoneClazz.class));
    }
}
