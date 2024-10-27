package net.hectus.neobb.turn.legacy_game.structure;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.hectus.neobb.util.MinecraftTime;

import java.util.List;

public class LTPumpkinWall extends StructureTurn implements BuffFunction, NatureClazz {
    public LTPumpkinWall(NeoPlayer player) { super(player); }
    public LTPumpkinWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("legacy-pumpkin_wall");
    }

    @Override
    public void apply() {
        player.game.time(MinecraftTime.MIDNIGHT);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 10));
    }
}
