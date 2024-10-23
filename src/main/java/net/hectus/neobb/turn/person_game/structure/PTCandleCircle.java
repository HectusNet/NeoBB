package net.hectus.neobb.turn.person_game.structure;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.hectus.neobb.turn.person_game.categorization.ArmorCategory;
import net.hectus.neobb.turn.person_game.other.PTArmorStand;

import java.util.List;

public class PTCandleCircle extends StructureTurn implements ArmorCategory {
    public PTCandleCircle(NeoPlayer player) { super(player); }
    public PTCandleCircle(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("candle_circle");
    }

    @Override
    public int armor() {
        return 3;
    }

    @Override
    public List<Class<? extends Turn<?>>> counteredBy() {
        return List.of(PTArmorStand.class);
    }
}
