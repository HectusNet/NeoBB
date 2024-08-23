package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TWater extends BlockTurn implements CounterFunction, WaterClazz {
    public TWater(NeoPlayer player) { super(player); }
    public TWater(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.WATER_BUCKET);
    }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(turn -> turn instanceof BlockTurn blockTurn && blockTurn.data().getBlockData() instanceof Waterlogged, "waterloggable"));
    }
}
