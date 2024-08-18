package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import org.bukkit.Material;

import java.util.List;

public class TDesertWarp extends Warp {
    public TDesertWarp(NeoPlayer player) { super(player, "desert"); }
    public TDesertWarp(PlacedStructure data, NeoPlayer player) { super(data, player, "desert"); }

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
        return Pair.of(Material.SAND, Material.SUSPICIOUS_SAND);
    }

    @Override
    public int cost() {
        return 4;
    }
}
