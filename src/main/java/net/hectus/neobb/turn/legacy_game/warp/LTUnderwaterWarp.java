package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import org.bukkit.World;

import java.util.List;

public class LTUnderwaterWarp extends LWarpTurn {
    public LTUnderwaterWarp(World world) { super(world, "legacy-underwater"); }
    public LTUnderwaterWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "legacy-underwater"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 8);
    }

    @Override
    public int chance() {
        return 73;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, ColdClazz.class, WaterClazz.class, NatureClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
