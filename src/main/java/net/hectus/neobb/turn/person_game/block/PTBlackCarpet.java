package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.CounterCategory;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class PTBlackCarpet extends BlockTurn implements CounterCategory {
    public PTBlackCarpet(NeoPlayer player) { super(player); }
    public PTBlackCarpet(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public void counter(@NotNull NeoPlayer source, @NotNull Turn<?> countered) {
        CounterCategory.super.counter(source, countered);
        countered.player().damage(countered.damage() + 2);
    }
}
