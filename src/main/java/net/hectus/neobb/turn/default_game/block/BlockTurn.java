package net.hectus.neobb.turn.default_game.block;

import com.marcpg.libpg.storing.Cord;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public abstract class BlockTurn extends Turn<Block> {
    public BlockTurn(NeoPlayer player) { super(player); }
    public BlockTurn(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.valueOf(Utilities.camelToSnake(Utilities.counterFilterName(getClass().getSimpleName())).toUpperCase()));
    }

    @Override
    public Cord cord() {
        return cord.add(new Cord(0.5, 0.5, 0.5));
    }
}
