package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class TOceanWarp extends Warp {
    public TOceanWarp(World world) { super(world, "ocean"); }
    public TOceanWarp(PlacedStructure data, World world) { super(data, world, "ocean"); }

    @Override
    public int chance() {
        return 70;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, WaterClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.DARK_PRISMARINE, Material.SEA_LANTERN);
    }

    @Override
    public int cost() {
        return 4;
    }
}
