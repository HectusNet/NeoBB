package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.player.NeoInventory;
import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

import java.util.List;

public class TPoppy extends FlowerTurn {
    public TPoppy(NeoPlayer player) { super(player); }
    public TPoppy(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public void apply() {
        List<NeoPlayer> players = player.game.players();

        NeoInventory temp = players.getFirst().inventory;
        for (int i = 0; i < players.size() - 1; i++) {
            players.get(i).inventory = players.get(i + 1).inventory;
        }
        players.getLast().inventory = temp;

        players.forEach(p -> p.inventory.switchOwner(p));
    }
}
