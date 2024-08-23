package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import org.bukkit.DyeColor;
import org.bukkit.entity.Evoker;

import java.util.List;

public class TEvoker extends MobTurn<Evoker> implements CounterbuffFunction, SupernaturalClazz {
    public TEvoker(NeoPlayer player) { super(player); }
    public TEvoker(Evoker data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 20), new Buff.ExtraTurn());
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(turn -> turn instanceof TSheep t && t.data().getColor() == DyeColor.BLUE, "blue-sheep"));
    }
}
