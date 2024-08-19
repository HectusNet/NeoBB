package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class TSunWarp extends Warp {
    public TSunWarp(World world) { super(world, "sun"); }
    public TSunWarp(PlacedStructure data, World world) { super(data, world, "sun"); }

    @Override
    public int chance() {
        return 40;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(HotClazz.class, RedstoneClazz.class, SupernaturalClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.OCHRE_FROGLIGHT, Material.SHROOMLIGHT);
    }

    @Override
    public int cost() {
        return 4;
    }
}
