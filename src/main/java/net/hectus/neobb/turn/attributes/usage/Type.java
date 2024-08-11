package net.hectus.neobb.turn.attributes.usage;

import net.hectus.neobb.turn.CounterFilter;

public interface Type<T> extends CounterFilter {
    T getValue();
}
