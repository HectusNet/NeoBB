package net.hectus.neobb.turn.legacy_game.structure.glass_wall;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.structure.glass_wall.GlassWallTurn;

public class LTLimeGlassWall extends GlassWallTurn implements NatureClazz {
    public LTLimeGlassWall(NeoPlayer player) { super(player); }
    public LTLimeGlassWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public String color() {
        return "lime";
    }
}
