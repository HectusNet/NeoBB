package net.hectus.neobb.game;

import net.hectus.neobb.NeoBB;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.game.util.Difficulty;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.function.*;
import net.hectus.neobb.turn.default_game.mob.TPhantom;
import net.hectus.neobb.turn.default_game.warp.WarpTurn;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class HectusGame extends BossBarGame {
    public HectusGame(Difficulty difficulty, World world, @NotNull List<Player> players, WarpTurn defaultWarp) {
        super(difficulty, world, players, defaultWarp);
    }

    @Override
    public boolean executeTurn(Turn<?> turn) {
        if (turn instanceof CounterFunction counter) {
            if (counter.counterLogic(turn)) {
                NeoBB.LOG.info("{}: Not applying counter from turn.", id);
                return false;
            } else {
                NeoBB.LOG.info("{}: Applying counter from turn.", id);
            }
        }

        if (turn instanceof AttackFunction) {
            if (!(getNextPlayer().hasModifier(Modifiers.P_DEFAULT_DEFENDED) || turn instanceof TPhantom)) {
                NeoBB.LOG.info("{}: Applying attack from turn.", id);
                getNextPlayer().addModifier(Modifiers.P_DEFAULT_ATTACKED);
            } else {
                NeoBB.LOG.info("{}: Not applying attack from turn, due to defense.", id);
            }
        }

        if (turn instanceof BuffFunction buffFunction) {
            List<Buff> buffs = buffFunction.buffs();
            NeoBB.LOG.info("{}: Applying {} buffs from turn.", id, buffs.size());
            buffs.forEach(b -> b.apply(turn.player()));
        }
        if (turn instanceof DefenseFunction defenseFunction) {
            NeoBB.LOG.info("{}: Applying defense from turn.", id);
            defenseFunction.applyDefense();
        }
        if (turn instanceof EventFunction eventFunction) {
            NeoBB.LOG.info("{}: Triggering event from turn.", id);
            eventFunction.triggerEvent();
        }

        return true;
    }
}
