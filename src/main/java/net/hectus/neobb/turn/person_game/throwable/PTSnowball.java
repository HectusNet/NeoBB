package net.hectus.neobb.turn.person_game.throwable;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.throwable.ThrowableTurn;
import net.hectus.neobb.turn.person_game.categorization.SituationalAttackCategory;
import net.hectus.neobb.turn.person_game.warp.PTSnowWarp;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;

public class PTSnowball extends ThrowableTurn implements SituationalAttackCategory {
    public PTSnowball(NeoPlayer player) { super(player); }
    public PTSnowball(Projectile data, Location impact, NeoPlayer player) { super(data, impact, player); }

    @Override
    public void apply() {
        super.apply();
        new Buff.ExtraTurn().apply(player);
    }

    @Override
    public double damage() {
        return 2;
    }

    @Override
    public boolean unusable() {
        return !(player.game.warp() instanceof PTSnowWarp);
    }
}
