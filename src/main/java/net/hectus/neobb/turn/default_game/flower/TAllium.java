package net.hectus.neobb.turn.default_game.flower;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

public class TAllium extends FlowerTurn {
    public TAllium(NeoPlayer player) { super(player); }
    public TAllium(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 7;
    }

    @Override
    public void apply() {
        for (NeoPlayer p : player.game.players()) {
            if (p.inventory.isEmpty()) continue;

            ItemStack[] deck = p.inventory.deck();
            p.inventory.setDeckSlot(Randomizer.fromCollection(IntStream.range(0, deck.length)
                    .filter(i -> deck[i] != null)
                    .boxed()
                    .toList()
            ), null, null);
        }
    }
}
