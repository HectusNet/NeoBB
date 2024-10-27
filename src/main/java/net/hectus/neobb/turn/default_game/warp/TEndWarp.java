package net.hectus.neobb.turn.default_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import org.bukkit.World;

import java.util.List;

public class TEndWarp extends WarpTurn {
    public TEndWarp(World world) { super(world, "end"); }
    public TEndWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "end"); }

    @Override
    public int chance() {
        return 60;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(ColdClazz.class, SupernaturalClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
