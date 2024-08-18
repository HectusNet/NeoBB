package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import org.bukkit.Material;

import java.util.List;

public class TAmethystWarp extends Warp {
    public TAmethystWarp(NeoPlayer player) { super(player, "amethyst"); }
    public TAmethystWarp(PlacedStructure data, NeoPlayer player) { super(data, player, "amethyst"); }

    @Override
    public int chance() {
        return 20;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, WaterClazz.class, NatureClazz.class, SupernaturalClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.AMETHYST_BLOCK, Material.BUDDING_AMETHYST);
    }

    @Override
    public int cost() {
        return 4;
    }
}
