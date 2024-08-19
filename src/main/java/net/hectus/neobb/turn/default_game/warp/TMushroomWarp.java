package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class TMushroomWarp extends Warp {
    public TMushroomWarp(World world) { super(world, "mushroom"); }
    public TMushroomWarp(PlacedStructure data, World world) { super(data, world, "mushroom"); }

    @Override
    public int chance() {
        return 50;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, NatureClazz.class, SupernaturalClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.BROWN_MUSHROOM_BLOCK, Material.RED_MUSHROOM_BLOCK);
    }

    @Override
    public int cost() {
        return 4;
    }
}
