package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import org.bukkit.block.Block;

import java.util.List;

public class TWitherRose extends FlowerTurn {
    public TWitherRose(NeoPlayer player) { super(player); }
    public TWitherRose(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public void apply() {
        player.game.allowedClazzes().remove(SupernaturalClazz.class);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.NEXT, -15));
    }
}
