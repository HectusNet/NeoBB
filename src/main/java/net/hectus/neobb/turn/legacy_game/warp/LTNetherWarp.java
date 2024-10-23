package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import org.bukkit.World;

import java.util.List;

public class LTNetherWarp extends LWarpTurn {
    public LTNetherWarp(World world) { super(world, "nether"); }
    public LTNetherWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "nether"); }

    @Override
    public int chance() {
        return 80;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, HotClazz.class, RedstoneClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
