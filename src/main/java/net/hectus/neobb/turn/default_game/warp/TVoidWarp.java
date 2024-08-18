package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import org.bukkit.Material;

import java.util.List;

public class TVoidWarp extends Warp {
    public TVoidWarp(NeoPlayer player) { super(player, "void"); }
    public TVoidWarp(PlacedStructure data, NeoPlayer player) { super(data, player, "void"); }

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
