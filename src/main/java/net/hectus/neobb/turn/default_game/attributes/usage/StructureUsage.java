package net.hectus.neobb.turn.default_game.attributes.usage;

import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;

public interface StructureUsage extends Type<PlacedStructure> {
    Structure referenceStructure();
}
