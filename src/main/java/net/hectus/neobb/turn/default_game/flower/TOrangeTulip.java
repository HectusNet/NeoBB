package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import org.bukkit.block.Block;

import java.util.List;

public class TOrangeTulip extends FlowerTurn {
    public TOrangeTulip(NeoPlayer player) { super(player); }
    public TOrangeTulip(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public void apply() {
        player.game.allowedClazzes().add(HotClazz.class);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn());
    }
}
