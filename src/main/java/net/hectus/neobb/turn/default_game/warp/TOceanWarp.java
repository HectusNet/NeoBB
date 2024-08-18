package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.Material;

import java.util.List;

public class TOceanWarp extends Warp {
    public TOceanWarp(NeoPlayer player) { super(player, "ocean"); }
    public TOceanWarp(PlacedStructure data, NeoPlayer player) { super(data, player, "ocean"); }

    @Override
    public int chance() {
        return 70;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, WaterClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.DARK_PRISMARINE, Material.SEA_LANTERN);
    }

    @Override
    public int cost() {
        return 4;
    }
}
