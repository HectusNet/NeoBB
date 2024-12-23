package net.hectus.neobb.turn.default_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import org.bukkit.World;

import java.util.List;

public class TRedstoneWarp extends WarpTurn {
    public TRedstoneWarp(World world) { super(world, "redstone"); }
    public TRedstoneWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "redstone"); }

    @Override
    public int chance() {
        return 60;
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
