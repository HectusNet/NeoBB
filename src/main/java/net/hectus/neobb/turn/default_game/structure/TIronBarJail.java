package net.hectus.neobb.turn.default_game.structure;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.util.Modifiers;

import java.util.List;

public class TIronBarJail extends StructureTurn implements BuffFunction, NeutralClazz {
    protected NeoPlayer trapped = null;

    public TIronBarJail(NeoPlayer player) { super(player); }
    public TIronBarJail(PlacedStructure data, NeoPlayer player, NeoPlayer trapped) {
        super(data, player);
        this.trapped = trapped;
    }

    @Override
    public int cost() {
        return 7;
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("iron_bar_jail");
    }

    @Override
    public void apply() {
        if (trapped == null) return;

        trapped.addModifier(Modifiers.P_NO_MOVE);
        trapped.addModifier(Modifiers.P_DEFAULT_NO_ATTACK);

        player.game.turnScheduler.runTaskTimer("iron_bar_jail", () -> {
            if (player.game.history().getLast() instanceof CounterFunction &&
                    CounterFilter.clazz(RedstoneClazz.class).doCounter(player.game.history().getLast()) ||
                    CounterFilter.clazz(SupernaturalClazz.class).doCounter(player.game.history().getLast())) {
                trapped.removeModifier(Modifiers.P_NO_MOVE);
                trapped.removeModifier(Modifiers.P_DEFAULT_NO_ATTACK);
            }
        }, () -> !trapped.hasModifier(Modifiers.P_NO_MOVE) || !trapped.hasModifier(Modifiers.P_DEFAULT_NO_ATTACK), 1);
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }
}
