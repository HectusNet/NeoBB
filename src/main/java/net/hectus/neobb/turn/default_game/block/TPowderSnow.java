package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TPowderSnow extends BlockTurn implements BuffFunction, ColdClazz {
    public TPowderSnow(NeoPlayer player) { super(player); }
    public TPowderSnow(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.POWDER_SNOW_BUCKET);
    }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public void apply() {
        player.nextPlayer().player.setFreezeTicks(6000); // 5 Minutes Max
        player.game.turnScheduler.runTaskLater("freeze", () -> player.game.eliminatePlayer(player.nextPlayer()), 3);
    }

    @Override
    public int maxAmount() { return 1; }

    @Override
    public List<Buff> buffs() { return List.of(); }
}
