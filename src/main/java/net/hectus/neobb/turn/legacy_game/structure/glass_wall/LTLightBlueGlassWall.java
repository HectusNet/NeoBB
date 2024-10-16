package net.hectus.neobb.turn.legacy_game.structure.glass_wall;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.structure.glass_wall.GlassWallTurn;

public class LTLightBlueGlassWall extends GlassWallTurn implements ColdClazz {
    public LTLightBlueGlassWall(NeoPlayer player) { super(player); }
    public LTLightBlueGlassWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public String color() {
        return "light_blue";
    }
}
