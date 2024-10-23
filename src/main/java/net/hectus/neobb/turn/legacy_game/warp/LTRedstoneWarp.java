package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import org.bukkit.World;

import java.util.List;

public class LTRedstoneWarp extends LWarpTurn {
    public LTRedstoneWarp(World world) { super(world, "redstone"); }
    public LTRedstoneWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "redstone"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 5);
    }

    @Override
    public int chance() {
        return 65;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, HotClazz.class, ColdClazz.class, RedstoneClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
