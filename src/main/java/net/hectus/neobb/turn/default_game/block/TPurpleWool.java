package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TPurpleWool extends Turn<Block> implements BlockUsage, AttackFunction, NeutralClazz {
    public TPurpleWool(NeoPlayer player) { super(null, null, player); }
    public TPurpleWool(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

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
