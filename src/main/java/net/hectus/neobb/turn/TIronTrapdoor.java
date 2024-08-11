package net.hectus.neobb.turn;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.attributes.function.CounterattackFunction;
import net.hectus.neobb.turn.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TIronTrapdoor extends Turn<Block> implements BlockUsage, CounterattackFunction, NeutralClazz {
    public TIronTrapdoor(NeoPlayer player) { super(null, player); }
    public TIronTrapdoor(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.IRON_TRAPDOOR);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<Class<? extends CounterFilter>> counters() {
        return List.of(NeutralClazz.class, WaterClazz.class, RedstoneClazz.class);
    }

    @Override
    public Block getValue() {
        return data;
    }
}
