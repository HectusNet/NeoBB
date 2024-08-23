package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;

import java.util.List;

public class TMagentaGlazedTerracotta extends BlockTurn implements CounterattackFunction, NeutralClazz {
    public TMagentaGlazedTerracotta(NeoPlayer player) { super(player); }
    public TMagentaGlazedTerracotta(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public boolean requiresUsageGuide() { return true; }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public boolean canBeUsed() {
        if (!player.game.history().isEmpty() && player.game.history().getLast() instanceof BlockTurn blockTurn) {
            return data.getRelative(((Directional) data.getBlockData()).getFacing()).equals(blockTurn.data());
        } else {
            return false;
        }
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(HotClazz.class), CounterFilter.clazz(ColdClazz.class), CounterFilter.clazz(WaterClazz.class), CounterFilter.clazz(NatureClazz.class));
    }
}
