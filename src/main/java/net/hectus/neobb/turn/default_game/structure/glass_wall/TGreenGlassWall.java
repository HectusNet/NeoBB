package net.hectus.neobb.turn.default_game.structure.glass_wall;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;

public class TGreenGlassWall extends GlassWallTurn implements NatureClazz {
    public TGreenGlassWall(NeoPlayer player) { super(player); }
    public TGreenGlassWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public String color() {
        return "green";
    }
}
