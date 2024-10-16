package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import org.bukkit.World;

import java.util.List;

public class LTIceWarp extends LWarpTurn {
    public LTIceWarp(World world) { super(world, "legacy-ice"); }
    public LTIceWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "legacy-ice"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 5);
    }

    @Override
    public int chance() {
        return 69;
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
