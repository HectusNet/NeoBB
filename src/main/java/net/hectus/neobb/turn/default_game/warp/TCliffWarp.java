package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class TCliffWarp extends Warp {
    public TCliffWarp(World world) { super(world, "cliff"); }
    public TCliffWarp(PlacedStructure data, World world) { super(data, world, "cliff"); }

    @Override
    public int chance() {
        return 60;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, RedstoneClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.STONE, Material.SNOW_BLOCK);
    }

    @Override
    public int cost() {
        return 4;
    }
}
