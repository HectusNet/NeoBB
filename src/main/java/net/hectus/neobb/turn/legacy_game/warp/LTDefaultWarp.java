package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import org.bukkit.World;

import java.util.List;

public class LTDefaultWarp extends LWarpTurn {
    public LTDefaultWarp(World world) { super(world, "legacy-default"); }

    @Override
    public int chance() {
        return 0;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, WaterClazz.class, NatureClazz.class, RedstoneClazz.class);
    }

    @Override
    public int cost() {
        return 0;
    }
}
