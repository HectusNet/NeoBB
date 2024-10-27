package net.hectus.neobb.turn.default_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.World;

import java.util.List;

public class TOceanWarp extends WarpTurn {
    public TOceanWarp(World world) { super(world, "ocean"); }
    public TOceanWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "ocean"); }

    @Override
    public int chance() {
        return 70;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, WaterClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
