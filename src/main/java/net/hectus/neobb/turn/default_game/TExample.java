package net.hectus.neobb.turn.default_game;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class TExample extends Turn<Block> implements BlockUsage {
    public TExample(NeoPlayer player) { super(null, null, player); }
    public TExample(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.AIR);
    }

    @Override
    public int cost() {
        return 0;
    }

    @Override
    public Block getValue() {
        return data;
    }
}
