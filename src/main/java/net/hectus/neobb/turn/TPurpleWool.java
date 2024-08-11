package net.hectus.neobb.turn;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.attributes.function.AttackFunction;
import net.hectus.neobb.turn.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TPurpleWool extends Turn<Block> implements BlockUsage, AttackFunction, NeutralClazz {
    public TPurpleWool(NeoPlayer player) { super(null, player); }
    public TPurpleWool(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public boolean canBeUsed() {
        List<Turn<?>> history = player.game.history();
        if (history.size() < 5) return false;

        return history.subList(history.size() - 5, history.size()).stream()
                .noneMatch(turn -> turn instanceof AttackFunction);
    }

    @Override
    public void apply() {
        player.game.win(player);
    }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.PURPLE_WOOL);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public Block getValue() {
        return data;
    }
}
