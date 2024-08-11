package net.hectus.neobb.turn;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.attributes.function.AttackFunction;
import net.hectus.neobb.turn.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class TCauldron extends Turn<Block> implements BlockUsage, AttackFunction, NeutralClazz {
    public TCauldron(NeoPlayer player) { super(null, player); }
    public TCauldron(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.CAULDRON);
    }

    @Override
    public int cost() {
        return 5; // TODO: Set actual cauldron cost.
    }

    @Override
    public Block getValue() {
        return data;
    }
}
