package net.hectus.neobb.turn.default_game.structure.glass_wall;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;

public class TOrangeGlassWall extends GlassWallTurn implements HotClazz {
    public TOrangeGlassWall(NeoPlayer player) { super(player); }
    public TOrangeGlassWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public String color() {
        return "orange";
    }
}
