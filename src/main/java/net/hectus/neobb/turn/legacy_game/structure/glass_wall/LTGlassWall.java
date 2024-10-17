package net.hectus.neobb.turn.legacy_game.structure.glass_wall;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.structure.glass_wall.GlassWallTurn;
import net.hectus.neobb.util.Modifiers;

import java.util.List;

public class LTGlassWall extends GlassWallTurn implements NeutralClazz {
    public LTGlassWall(NeoPlayer player) { super(player); }
    public LTGlassWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("glass_wall");
    }

    @Override
    public String color() {
        return "";
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }

    @Override
    public void applyDefense() {
        player.addModifier(Modifiers.P_DEFAULT_DEFENDED);
    }
}
