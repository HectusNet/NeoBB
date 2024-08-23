package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TFenceGate extends BlockTurn implements CounterattackFunction, RedstoneClazz {
    public TFenceGate(NeoPlayer player) { super(player); }
    public TFenceGate(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.OAK_FENCE_GATE);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(HotClazz.class));
    }
}
