package net.hectus.neobb.game.util;

import com.marcpg.libpg.data.time.Time;
import net.hectus.neobb.turn.Turn;

import java.util.List;

public record GameInfo(int coins, int deckSize, Time totalTime, int turnTimer, List<Class<? extends Turn<?>>> turns) {}
