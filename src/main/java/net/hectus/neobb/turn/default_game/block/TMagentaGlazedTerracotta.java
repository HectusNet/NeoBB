package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TMagentaGlazedTerracotta extends Turn<Block> implements BlockUsage, CounterattackFunction, NeutralClazz {
    public TMagentaGlazedTerracotta(NeoPlayer player) { super(null, null, player); }
    public TMagentaGlazedTerracotta(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public Block getValue() {
        return data;
    }

    @Override
    public boolean canBeUsed() {
        if (!player.game.history().isEmpty() && player.game.history().getLast() instanceof BlockUsage blockUsage) {
            return data.getRelative(((Directional) data.getBlockData()).getFacing()).equals(blockUsage.getValue());
        } else {
            return false;
        }
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(HotClazz.class), CounterFilter.clazz(ColdClazz.class), CounterFilter.clazz(WaterClazz.class), CounterFilter.clazz(NatureClazz.class));
    }
}
