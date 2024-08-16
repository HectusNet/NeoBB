package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import net.hectus.neobb.turn.default_game.CounterFilter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TNetherrack extends Turn<Block> implements BlockUsage, CounterattackFunction, HotClazz {
    public TNetherrack(NeoPlayer player) { super(null, null, player); }
    public TNetherrack(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.NETHERRACK);
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
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(HotClazz.class), CounterFilter.clazz(ColdClazz.class), CounterFilter.clazz(WaterClazz.class));
    }
}
