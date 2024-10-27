package net.hectus.neobb.turn.person_game.structure;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.hectus.neobb.turn.person_game.categorization.UtilityCategory;
import net.hectus.neobb.util.MinecraftTime;

public class PTPumpkinWall extends StructureTurn implements UtilityCategory {
    public PTPumpkinWall(NeoPlayer player) { super(player); }
    public PTPumpkinWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("pumpkin_wall");
    }

    @Override
    public void apply() {
        super.apply();
        player.game.time(MinecraftTime.MIDNIGHT);
    }
}
