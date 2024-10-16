package net.hectus.neobb.turn.default_game.structure;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.EventFunction;
import net.hectus.neobb.util.Modifiers;

public class TRedstoneWall extends StructureTurn implements EventFunction, RedstoneClazz {
    public TRedstoneWall(NeoPlayer player) { super(player); }
    public TRedstoneWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("redstone_wall");
    }

    @Override
    public void triggerEvent() {
        player.game.addModifier(Modifiers.G_DEFAULT_REDSTONE_POWER); // TODO: Power all blocks if this modifier is present.
        player.game.turnScheduler.runTaskLater("redstone_power", () -> player.game.removeModifier(Modifiers.G_DEFAULT_REDSTONE_POWER), 3);
    }
}
