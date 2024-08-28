package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.buff.ChancedBuff;
import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

import java.util.List;

public class TOxeyeDaisy extends FlowerTurn {
    public TOxeyeDaisy(NeoPlayer player) { super(player); }
    public TOxeyeDaisy(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new ChancedBuff(new Buff.ExtraTurn(), 25));
    }
}
