package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class TPackedIce extends Turn<Block> implements BlockUsage, AttackFunction, ColdClazz {
    public TPackedIce(NeoPlayer player) { super(null, null, player); }
    public TPackedIce(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.PACKED_ICE);
    }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public Block getValue() {
        return data;
    }
}
