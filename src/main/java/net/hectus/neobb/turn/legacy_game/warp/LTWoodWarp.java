package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.World;

import java.util.List;

public class LTWoodWarp extends LWarpTurn {
    public LTWoodWarp(World world) { super(world, "legacy-wood"); }
    public LTWoodWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "legacy-wood"); }

    @Override
    public int chance() {
        return 100;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, NatureClazz.class, WaterClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
