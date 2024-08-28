package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.List;

public class TMeadowWarp extends WarpTurn {
    public TMeadowWarp(World world) { super(world, "meadow"); }
    public TMeadowWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "meadow"); }

    @Override
    public int chance() {
        return 30;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, NatureClazz.class, SupernaturalClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.GRASS_BLOCK, Material.EMERALD_ORE);
    }

    @Override
    public int cost() {
        return 4;
    }
}
