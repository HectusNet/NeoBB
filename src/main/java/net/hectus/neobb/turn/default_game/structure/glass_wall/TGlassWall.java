package net.hectus.neobb.turn.default_game.structure.glass_wall;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TGlassWall extends GlassWallTurn implements NeutralClazz {
    public TGlassWall(NeoPlayer player) { super(player); }
    public TGlassWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("glass_wall");
    }

    @Override
    public String color() {
        return "";
    }
}
