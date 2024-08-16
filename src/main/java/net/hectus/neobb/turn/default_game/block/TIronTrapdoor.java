package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import net.hectus.neobb.turn.default_game.CounterFilter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TIronTrapdoor extends Turn<Block> implements BlockUsage, CounterattackFunction, NeutralClazz {
    public TIronTrapdoor(NeoPlayer player) { super(null, null, player); }
    public TIronTrapdoor(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.IRON_TRAPDOOR);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(WaterClazz.class), CounterFilter.clazz(RedstoneClazz.class));
    }

    @Override
    public Block getValue() {
        return data;
    }
}
