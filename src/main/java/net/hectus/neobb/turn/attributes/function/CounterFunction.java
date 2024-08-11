package net.hectus.neobb.turn.attributes.function;

import net.hectus.neobb.turn.CounterFilter;

import java.util.List;

public interface CounterFunction extends Function {
    List<Class<? extends CounterFilter>> counters();
}
