package net.hectus.bb.util;

import java.util.ArrayList;
import java.util.List;

public class Modifiers {
    private final List<String> modifiers = new ArrayList<>();

    public void enable(String modifier) {
        if (modifiers.contains(modifier)) return;
        modifiers.add(modifier);
    }

    public void disable(String modifier) {
        modifiers.remove(modifier);
    }

    public boolean isEnabled(String modifier) {
        return modifiers.contains(modifier);
    }

    @Override
    public String toString() {
        return modifiers.toString();
    }
}
