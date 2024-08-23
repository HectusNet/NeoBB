package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import org.bukkit.entity.Phantom;

public class TPhantom extends MobTurn<Phantom> implements AttackFunction, SupernaturalClazz {
    public TPhantom(NeoPlayer player) { super(player); }
    public TPhantom(Phantom data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }
}
