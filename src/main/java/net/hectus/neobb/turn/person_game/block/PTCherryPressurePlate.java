package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.CounterCategory;
import org.bukkit.block.Block;

public class PTCherryPressurePlate extends BlockTurn implements CounterCategory {
    public PTCherryPressurePlate(NeoPlayer player) { super(player); }
    public PTCherryPressurePlate(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public void apply() {
        super.apply();
        player.addArmor(1);
    }
}
