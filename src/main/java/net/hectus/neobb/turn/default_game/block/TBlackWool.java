package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TBlackWool extends BlockTurn implements CounterattackFunction, BuffFunction, NeutralClazz {
    public TBlackWool(NeoPlayer player) { super(player); }
    public TBlackWool(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(ColdClazz.class), CounterFilter.clazz(SupernaturalClazz.class));
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.ALL, PotionEffectType.BLINDNESS));
    }
}
