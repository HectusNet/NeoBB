package net.hectus.neobb.turn.person_game.structure;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.hectus.neobb.turn.person_game.categorization.DefensiveCategory;
import net.hectus.neobb.util.Modifiers;

public class PTWoodWall extends StructureTurn implements DefensiveCategory {
    public PTWoodWall(NeoPlayer player) { super(player); }
    public PTWoodWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("wood_wall");
    }

    @Override
    public void apply() {
        super.apply();
        player.heal(1.0);
        if (Randomizer.boolByChance(15))
            new Buff.ExtraTurn().apply(player);
    }

    @Override
    public void applyDefense() {
        player.addModifier(Modifiers.P_DEFAULT_DEFENDED);
    }
}
