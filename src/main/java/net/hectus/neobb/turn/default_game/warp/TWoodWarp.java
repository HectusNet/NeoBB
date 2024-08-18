package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import org.bukkit.Material;

import java.util.List;

public class TWoodWarp extends Warp {
    public TWoodWarp(NeoPlayer player) { super(player, "wood"); }
    public TWoodWarp(PlacedStructure data, NeoPlayer player) { super(data, player, "wood"); }

    @Override
    public int chance() {
        return 100;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, NatureClazz.class);
    }

    @Override
    public Pair<Material, Material> materials() {
        return Pair.of(Material.OAK_LEAVES, Material.OAK_LOG);
    }

    @Override
    public int cost() {
        return 4;
    }
}
