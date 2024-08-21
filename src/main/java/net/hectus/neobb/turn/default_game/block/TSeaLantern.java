package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TSeaLantern extends Turn<Block> implements BlockUsage, BuffFunction, WaterClazz {
    public TSeaLantern(NeoPlayer player) { super(null, null, player); }
    public TSeaLantern(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.SEA_LANTERN);
    }

    @Override
    public int cost() {
        return 7;
    }

    @Override
    public Block getValue() {
        return data;
    }

    @Override
    public void apply() {
        // TODO: Give revive for next 3 turns.
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }
}
