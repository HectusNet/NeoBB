package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.AttackCategory;
import org.bukkit.block.Block;

import java.util.List;

public class PTOrangeWool extends BlockTurn implements AttackCategory {
    public PTOrangeWool(NeoPlayer player) { super(player); }
    public PTOrangeWool(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 10);
    }

    @Override
    public double damage() {
        return 2;
    }

    @Override
    public List<Class<? extends Turn<?>>> counteredBy() {
        return List.of(PTGreenCarpet.class, PTBarrel.class);
    }
}
