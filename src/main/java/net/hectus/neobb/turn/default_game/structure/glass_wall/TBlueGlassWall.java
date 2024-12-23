package net.hectus.neobb.turn.default_game.structure.glass_wall;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;

public class TBlueGlassWall extends GlassWallTurn implements WaterClazz {
    public TBlueGlassWall(NeoPlayer player) { super(player); }
    public TBlueGlassWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public String color() {
        return "blue";
    }
}
