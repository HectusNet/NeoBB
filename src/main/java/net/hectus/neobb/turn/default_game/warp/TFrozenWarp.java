package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class TFrozenWarp extends Warp {
    public TFrozenWarp(World world) { super(world, "frozen"); }
    public TFrozenWarp(PlacedStructure data, World world) { super(data, world, "frozen"); }

    @Override
    public int chance() {
        return 70;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(ColdClazz.class, WaterClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.SNOW_BLOCK, Material.PACKED_ICE);
    }

    @Override
    public int cost() {
        return 4;
    }
}
