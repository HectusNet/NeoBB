package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import org.bukkit.World;

import java.util.List;

public class LTSnowWarp extends LWarpTurn {
    public LTSnowWarp(World world) { super(world, "snow"); }
    public LTSnowWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "snow"); }

    @Override
    public int chance() {
        return 80;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, ColdClazz.class, WaterClazz.class, SupernaturalClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
