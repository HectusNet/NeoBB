package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import org.bukkit.block.Block;

import java.util.List;

public class TWhiteTulip extends FlowerTurn {
    public TWhiteTulip(NeoPlayer player) { super(player); }
    public TWhiteTulip(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public void apply() {
        player.game.allowedClazzes().add(ColdClazz.class);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn());
    }
}
