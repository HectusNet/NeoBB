package net.hectus.neobb.turn.default_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import org.bukkit.World;

import java.util.List;

public class TVoidWarp extends WarpTurn {
    public TVoidWarp(World world) { super(world, "void"); }
    public TVoidWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "void"); }

    @Override
    public int chance() {
        return 10;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(ColdClazz.class, NeutralClazz.class, WaterClazz.class, RedstoneClazz.class, SupernaturalClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
