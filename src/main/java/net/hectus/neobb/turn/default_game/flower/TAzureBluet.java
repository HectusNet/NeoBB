package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

import java.util.List;

public class TAzureBluet extends FlowerTurn {
    public TAzureBluet(NeoPlayer player) { super(player); }
    public TAzureBluet(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 7;
    }

    @Override
    public boolean canBeUsed() {
        if (isDummy()) return false;
        return player.game.history().stream().anyMatch(t -> t instanceof TAzureBluet);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 20));
    }
}
