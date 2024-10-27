package net.hectus.neobb.turn.default_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.World;

import java.util.List;

public class TFrozenWarp extends WarpTurn {
    public TFrozenWarp(World world) { super(world, "frozen"); }
    public TFrozenWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "frozen"); }

    @Override
    public int chance() {
        return 70;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(ColdClazz.class, WaterClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
