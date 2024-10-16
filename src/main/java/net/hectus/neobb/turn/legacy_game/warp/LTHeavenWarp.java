package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import org.bukkit.World;

import java.util.List;

public class LTHeavenWarp extends LWarpTurn {
    public LTHeavenWarp(World world) { super(world, "legacy-heaven"); }
    public LTHeavenWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "legacy-heaven"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 20);
        new Buff.Luck(Buff.BuffTarget.OPPONENTS, 10);
    }

    @Override
    public int chance() {
        return 100; // Actually 55 or 45, depending on structure.
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, ColdClazz.class, NatureClazz.class, SupernaturalClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
