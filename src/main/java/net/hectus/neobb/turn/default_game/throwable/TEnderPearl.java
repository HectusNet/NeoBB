package net.hectus.neobb.turn.default_game.throwable;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TEnderPearl extends ThrowableTurn implements CounterFunction, NeutralClazz {
    public TEnderPearl(NeoPlayer player) { super(player); }
    public TEnderPearl(Projectile data, Location impact, NeoPlayer player) { super(data, impact, player); }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public void apply() {
        player.player.removePotionEffect(PotionEffectType.LEVITATION);
        new Buff.ExtraTurn().apply(player);
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(t -> t instanceof TSplashLevitationPotion, "levitation"), CounterFilter.of(turn -> turn.getClass().getSimpleName().endsWith("Wall"), "walls"));
    }
}
