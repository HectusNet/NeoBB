package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TStonecutter extends BlockTurn implements CounterFunction, NeutralClazz {
    public TStonecutter(NeoPlayer player) { super(player); }
    public TStonecutter(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(turn -> turn.getClass().getSimpleName().endsWith("Wall"), "walls"));
    }

    @Override
    public void counter(@NotNull NeoPlayer source) {
        source.opponents(true).forEach(p -> p.removeModifier(Modifiers.P_DEFAULT_DEFENDED));
    }
}
