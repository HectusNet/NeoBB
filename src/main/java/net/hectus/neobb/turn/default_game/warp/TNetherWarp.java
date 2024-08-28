package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class TNetherWarp extends WarpTurn {
    public TNetherWarp(World world) { super(world, "nether"); }
    public TNetherWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "nether"); }

    @Override
    public int chance() {
        return 80;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(HotClazz.class, RedstoneClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.NETHERRACK, Material.MAGMA_BLOCK);
    }

    @Override
    public int cost() {
        return 4;
    }
}
