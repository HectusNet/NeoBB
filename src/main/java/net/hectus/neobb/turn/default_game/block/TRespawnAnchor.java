package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.buff.ChancedBuff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TRespawnAnchor extends Turn<Block> implements BlockUsage, BuffFunction, HotClazz {
    public TRespawnAnchor(NeoPlayer player) { super(null, null, player); }
    public TRespawnAnchor(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public boolean requiresUsageGuide() { return true; }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.RESPAWN_ANCHOR);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public Block getValue() {
        return data;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Teleport(Buff.BuffTarget.YOU), new ChancedBuff(new Buff.ExtraTurn(), 50));
    }
}
