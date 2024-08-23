package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import org.bukkit.entity.PolarBear;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TPolarBear extends MobTurn<PolarBear> implements BuffFunction, ColdClazz {
    public TPolarBear(NeoPlayer player) { super(player); }
    public TPolarBear(PolarBear data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public void apply() {
        player.addModifier("no_jump");
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.ALL, PotionEffectType.SPEED, 3));
    }
}
