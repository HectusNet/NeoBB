package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import org.bukkit.entity.Blaze;

public class TBlaze extends MobTurn<Blaze> implements AttackFunction, HotClazz {
    public TBlaze(NeoPlayer player) { super(player); }
    public TBlaze(Blaze data, NeoPlayer player) { super(data, player); }

    @Override
    public void apply() {
        player.game.addModifier("warp-prevent.cold");
        player.game.addModifier("warp-prevent.water");
    }

    @Override
    public int cost() {
        return 5;
    }
}
