package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.util.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Bee;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TBee extends MobTurn<Bee> implements BuffFunction, NatureClazz {
    public TBee(NeoPlayer player) { super(player); }
    public TBee(Bee data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public void apply() {
        // TODO: Remove all flower effects.
        player.sendMessage(Component.text("This feature is not yet implemented.", Colors.NEGATIVE));
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.OPPONENTS, PotionEffectType.SLOWNESS));
    }
}
