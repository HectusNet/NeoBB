package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class TEndWarp extends Warp {
    public TEndWarp(World world) { super(world, "end"); }
    public TEndWarp(PlacedStructure data, World world) { super(data, world, "end"); }

    @Override
    public int chance() {
        return 60;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(ColdClazz.class, SupernaturalClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.END_STONE, Material.OBSIDIAN);
    }

    @Override
    public int cost() {
        return 4;
    }
}
