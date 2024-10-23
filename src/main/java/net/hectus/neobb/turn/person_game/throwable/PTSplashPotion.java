package net.hectus.neobb.turn.person_game.throwable;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.throwable.ThrowableTurn;
import net.hectus.neobb.turn.person_game.categorization.UtilityCategory;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowman;

public class PTSplashPotion extends ThrowableTurn implements UtilityCategory {
    public PTSplashPotion(NeoPlayer player) { super(player); }
    public PTSplashPotion(Projectile data, Location impact, NeoPlayer player) { super(data, impact, player); }

    @Override
    public void apply() {
        super.apply();
        player.game.removeModifier(Modifiers.G_PERSON_SNOW_GOLEM);
        player.game.world().getEntitiesByClass(Snowman.class).forEach(s -> s.setHealth(0.0));
    }
}
