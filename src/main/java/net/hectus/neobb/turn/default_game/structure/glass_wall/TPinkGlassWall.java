package net.hectus.neobb.turn.default_game.structure.glass_wall;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;

public class TPinkGlassWall extends GlassWallTurn implements SupernaturalClazz {
    public TPinkGlassWall(NeoPlayer player) { super(player); }
    public TPinkGlassWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public String color() {
        return "pink";
    }
}
