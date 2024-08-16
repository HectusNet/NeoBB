package net.hectus.neobb.turn.default_game.attributes.function;

import net.hectus.neobb.buff.Buff;

import java.util.List;

public interface BuffFunction extends Function {
    List<Buff> buffs();
}
