package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TLava extends BlockTurn implements BuffFunction, HotClazz {
    public TLava(NeoPlayer player) { super(player); }
    public TLava(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public boolean requiresUsageGuide() { return true; }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.LAVA_BUCKET);
    }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public void apply() {
        player.nextPlayer().player.setFireTicks(6000); // 5 Minutes Max
        player.game.turnScheduler.runTaskLater("burn", () -> player.game.eliminatePlayer(player.nextPlayer()), 3);
    }

    @Override
    public int maxAmount() { return 1; }

    @Override
    public List<Buff> buffs() { return List.of(); }
}
