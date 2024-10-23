package net.hectus.neobb.turn.person_game.structure;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.hectus.neobb.turn.person_game.categorization.SituationalAttackCategory;
import net.hectus.neobb.turn.person_game.warp.PTVillagerWarp;

public class PTTorchCircle extends StructureTurn implements SituationalAttackCategory {
    public PTTorchCircle(NeoPlayer player) { super(player); }
    public PTTorchCircle(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("torch_circle");
    }

    @Override
    public void apply() {
        super.apply();
        player.addArmor(4);
    }

    @Override
    public boolean unusable() {
        return !(player.game.warp() instanceof PTVillagerWarp);
    }
}
