package net.hectus.neobb.turn;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.attributes.function.CounterFunction;
import net.hectus.neobb.turn.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TSpruceTrapdoor extends Turn<Block> implements BlockUsage, CounterFunction, NeutralClazz {
    public TSpruceTrapdoor(NeoPlayer player) { super(null, player); }
    public TSpruceTrapdoor(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.SPRUCE_TRAPDOOR);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<Class<? extends CounterFilter>> counters() {
        return List.of(NeutralClazz.class, WaterClazz.class, NatureClazz.class);
    }

    @Override
    public Block getValue() {
        return data;
    }
}
