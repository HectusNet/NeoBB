package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class TRedstoneWarp extends Warp {
    public TRedstoneWarp(World world) { super(world, "redstone"); }
    public TRedstoneWarp(PlacedStructure data, World world) { super(data, world, "redstone"); }

    @Override
    public int chance() {
        return 60;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(HotClazz.class, RedstoneClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.REDSTONE_LAMP, Material.REDSTONE_BLOCK);
    }

    @Override
    public int cost() {
        return 4;
    }
}
