package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.AttackCategory;
import org.bukkit.block.Block;

import java.util.List;

public class PTBirchLog extends BlockTurn implements AttackCategory {
    public PTBirchLog(NeoPlayer player) { super(player); }
    public PTBirchLog(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public void apply() {
        super.apply();
        player.nextPlayer().piercingDamage(2);
    }

    @Override
    public List<Class<? extends Turn<?>>> counteredBy() {
        return List.of(PTPinkCarpet.class, PTGreenCarpet.class);
    }
}
