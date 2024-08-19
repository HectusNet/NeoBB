package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class TVoidWarp extends Warp {
    public TVoidWarp(World world) { super(world, "void"); }
    public TVoidWarp(PlacedStructure data, World world) { super(data, world, "void"); }

    @Override
    public int chance() {
        return 10;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(ColdClazz.class, NeutralClazz.class, WaterClazz.class, RedstoneClazz.class, SupernaturalClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.BLACK_STAINED_GLASS, Material.BLACK_CONCRETE);
    }

    @Override
    public int cost() {
        return 4;
    }
}
