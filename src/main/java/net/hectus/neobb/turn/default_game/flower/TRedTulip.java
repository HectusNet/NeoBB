package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import org.bukkit.block.Block;

import java.util.List;

public class TRedTulip extends FlowerTurn {
    public TRedTulip(NeoPlayer player) { super(player); }
    public TRedTulip(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public void apply() {
        player.game.allowedClazzes().add(RedstoneClazz.class);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn());
    }
}
