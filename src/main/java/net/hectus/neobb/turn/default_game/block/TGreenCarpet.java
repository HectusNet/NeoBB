package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TGreenCarpet extends BlockTurn implements CounterattackFunction, NeutralClazz {
    public TGreenCarpet(NeoPlayer player) { super(player); }
    public TGreenCarpet(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(turn -> turn.getClass().getSimpleName().endsWith("Wool"), "wool"));
    }
}
