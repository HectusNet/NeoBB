package net.hectus.neobb.turn.default_game.structure;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;

import java.util.List;

public class TPumpkinWall extends StructureTurn implements BuffFunction, NatureClazz {
    public TPumpkinWall(NeoPlayer player) { super(player); }
    public TPumpkinWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("pumpkin_wall");
    }

    @Override
    public void apply() {
        player.game.world().setTime(18000); // 18000 should be midnight according to this: https://minecraft.wiki/w/Commands/time#Arguments
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 10));
    }
}
