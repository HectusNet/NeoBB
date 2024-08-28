package net.hectus.neobb.turn.default_game.structure.glass_wall;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;

public class TRedGlassWall extends GlassWallTurn implements NeutralClazz {
    public TRedGlassWall(NeoPlayer player) { super(player); }
    public TRedGlassWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public String color() {
        return "red";
    }
}