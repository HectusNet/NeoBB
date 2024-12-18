package net.hectus.neobb.turn.default_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import org.bukkit.World;

import java.util.List;

public class TSunWarp extends WarpTurn {
    public TSunWarp(World world) { super(world, "sun"); }
    public TSunWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "sun"); }

    @Override
    public int chance() {
        return 40;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(HotClazz.class, RedstoneClazz.class, SupernaturalClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
