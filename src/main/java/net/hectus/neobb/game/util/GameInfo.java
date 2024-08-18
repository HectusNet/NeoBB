package net.hectus.neobb.game.util;

import net.hectus.neobb.turn.Turn;

import java.util.List;

public record GameInfo(int coins, int deckSize, boolean hasBossBar, List<Class<? extends Turn<?>>> turns) {}
