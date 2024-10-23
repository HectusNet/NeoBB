package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.BuffCategory;
import org.bukkit.block.Block;

import java.util.List;

public class PTCake extends BlockTurn implements BuffCategory {
    public PTCake(NeoPlayer player) { super(player); }
    public PTCake(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 20));
    }
}
