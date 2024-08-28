package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class TDefaultWarp extends WarpTurn {
    public TDefaultWarp(World world) { super(world, "default"); }
    public TDefaultWarp(PlacedStructure data, World world) { super(data, world, null, "default"); }

    @Override
    public int chance() {
        return 0;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, WaterClazz.class, NatureClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.BEDROCK, Material.BEDROCK);
    }

    @Override
    public int cost() {
        return 0;
    }
}
