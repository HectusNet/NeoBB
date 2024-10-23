package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.CounterCategory;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PTFenceGate extends BlockTurn implements CounterCategory {
    public PTFenceGate(NeoPlayer player) { super(player); }
    public PTFenceGate(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.OAK_FENCE_GATE);
    }

    @Override
    public void counter(@NotNull NeoPlayer source, @NotNull Turn<?> countered) {
        CounterCategory.super.counter(source, countered);
        countered.player().damage(countered.damage() + 2);
    }
}
