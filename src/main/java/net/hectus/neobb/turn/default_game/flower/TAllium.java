package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class TAllium extends FlowerTurn {
    public TAllium(NeoPlayer player) { super(player); }
    public TAllium(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 7;
    }

    @Override
    public void apply() {
        player.game.players().forEach(p -> p.inventory.removeRandom());
    }
}
