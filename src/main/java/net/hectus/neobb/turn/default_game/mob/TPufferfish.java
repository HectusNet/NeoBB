package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import org.bukkit.entity.PufferFish;

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
        // TODO: Make player poisoned for 2 turns and kill him afterwards, if not healed.
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }
}
