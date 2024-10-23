package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.UtilityCategory;
import org.bukkit.block.Block;

public class PTSeaLantern extends BlockTurn implements UtilityCategory {
    public PTSeaLantern(NeoPlayer player) { super(player); }
    public PTSeaLantern(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public void apply() {
        super.apply();
        player.addArmor(1);
    }
}
