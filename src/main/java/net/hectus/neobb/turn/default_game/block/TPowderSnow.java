package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TPowderSnow extends Turn<Block> implements BlockUsage, BuffFunction, ColdClazz {
    public TPowderSnow(NeoPlayer player) { super(null, null, player); }
    public TPowderSnow(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.POWDER_SNOW_BUCKET);
    }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public Block getValue() {
        return data;
    }

    @Override
    public void apply() {
        // TODO: Make player freeze for 3 turns and kill him afterwards, if not countered.
    }

    @Override
    public int maxAmount() { return 1; }

    @Override
    public List<Buff> buffs() { return List.of(); }
}
