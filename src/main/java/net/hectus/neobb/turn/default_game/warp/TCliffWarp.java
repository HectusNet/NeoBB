package net.hectus.neobb.turn.default_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import org.bukkit.World;

import java.util.List;

public class TCliffWarp extends WarpTurn {
    public TCliffWarp(World world) { super(world, "cliff"); }
    public TCliffWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "cliff"); }

    @Override
    public int chance() {
        return 60;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, RedstoneClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
