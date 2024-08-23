package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.function.EventFunction;
import org.bukkit.entity.Piglin;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TPiglin extends MobTurn<Piglin> implements EventFunction, HotClazz {
    public TPiglin(NeoPlayer player) { super(player); }
    public TPiglin(Piglin data, NeoPlayer player) { super(data, player); }

    @Override
    public boolean requiresUsageGuide() { return true; }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public void triggerEvent() {
        data.addScoreboardTag(player.game.id);
    }

    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.YOU, PotionEffectType.BLINDNESS), new Buff.Effect(Buff.BuffTarget.YOU, PotionEffectType.SLOWNESS), new Buff.Luck(Buff.BuffTarget.YOU, 30));
    }
}
