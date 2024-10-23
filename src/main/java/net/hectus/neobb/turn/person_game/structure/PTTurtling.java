package net.hectus.neobb.turn.person_game.structure;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.hectus.neobb.turn.person_game.block.PTLever;
import net.hectus.neobb.turn.person_game.categorization.ArmorCategory;

import java.util.List;

public class PTTurtling extends StructureTurn implements ArmorCategory {
    public PTTurtling(NeoPlayer player) { super(player); }
    public PTTurtling(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("oak_door_turtling");
    }

    @Override
    public int armor() {
        return 3;
    }

    @Override
    public List<Class<? extends Turn<?>>> counteredBy() {
        return List.of(PTLever.class);
    }
}
