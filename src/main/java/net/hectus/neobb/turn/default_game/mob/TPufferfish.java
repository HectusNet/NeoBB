package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import org.bukkit.entity.PufferFish;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TPufferfish extends MobTurn<PufferFish> implements BuffFunction, WaterClazz {
    public TPufferfish(NeoPlayer player) { super(player); }
    public TPufferfish(PufferFish data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public void apply() {
        player.game.turnScheduler.runTaskLater("poison", () -> {
            if (player.nextPlayer().player.hasPotionEffect(PotionEffectType.POISON))
                player.game.eliminatePlayer(player.nextPlayer());
        }, 2);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.NEXT, PotionEffectType.POISON));
    }
}
