package net.hectus.bb.turn.counter;

import com.marcpg.libpg.storing.Pair;
import net.hectus.bb.turn.TurnData;
import net.hectus.bb.turn.effective.Defense;
import org.bukkit.block.data.Waterlogged;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CounterFilter {
    boolean doCounter(@NotNull TurnData last);

    record EndingCounter(String ending) implements CounterFilter {
        @Override
        public boolean doCounter(@NotNull TurnData last) {
            return last.turn().name().endsWith(ending);
        }
    }

    class WallCounter implements CounterFilter {
        @Override
        public boolean doCounter(@NotNull TurnData last) {
            Pair<Boolean, Defense> defense = last.player().game().getOpponent(last.player()).getDefense();
            return defense.left() && defense.right().isWall;
        }
    }

    class WaterLoggableCounter implements CounterFilter {
        @Override
        public boolean doCounter(@NotNull TurnData last) {
            return last.turn().items.get(0).createBlockData() instanceof Waterlogged;
        }
    }
}
