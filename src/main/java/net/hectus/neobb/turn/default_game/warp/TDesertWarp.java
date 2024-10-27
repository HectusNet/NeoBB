package net.hectus.neobb.turn.default_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import org.bukkit.World;

import java.util.List;

public class TDesertWarp extends WarpTurn {
    public TDesertWarp(World world) { super(world, "desert"); }
    public TDesertWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "desert"); }

    @Override
    public int chance() {
        return 80;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(HotClazz.class, RedstoneClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
