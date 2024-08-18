package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.Material;

import java.util.List;

public class TDefaultWarp extends Warp {
    public TDefaultWarp(NeoPlayer player) { super(player, "default"); }
    public TDefaultWarp(PlacedStructure data, NeoPlayer player) { super(data, player, "default"); }

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
