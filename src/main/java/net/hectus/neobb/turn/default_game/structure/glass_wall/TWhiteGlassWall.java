package net.hectus.neobb.turn.default_game.structure.glass_wall;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;

public class TWhiteGlassWall extends GlassWallTurn implements ColdClazz {
    public TWhiteGlassWall(NeoPlayer player) { super(player); }
    public TWhiteGlassWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public String color() {
        return "white";
    }
}
