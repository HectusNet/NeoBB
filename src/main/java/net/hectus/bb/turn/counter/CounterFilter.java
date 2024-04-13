package net.hectus.bb.turn.counter;

import com.marcpg.libpg.storing.Pair;
import net.hectus.bb.turn.TurnData;
import net.hectus.bb.turn.effective.Defense;
import org.jetbrains.annotations.NotNull;

public interface CounterFilter {
    boolean doCounter(@NotNull TurnData turn);

    record EndingCounter(String ending) implements CounterFilter {
        @Override
        public boolean doCounter(@NotNull TurnData turn) {
            return turn.turn().name().endsWith(ending);
        }
    }

    class WallCounter implements CounterFilter {
        @Override
        public boolean doCounter(@NotNull TurnData turn) {
            Pair<Boolean, Defense> defense = turn.player().game().getOpponent(turn.player()).getDefense();
            return defense.left() && defense.right().isWall;
        }
    }
}
