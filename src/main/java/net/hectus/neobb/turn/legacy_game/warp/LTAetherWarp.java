package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.World;

import java.util.List;

public class LTAetherWarp extends LWarpTurn {
    public LTAetherWarp(World world) { super(world, "aether"); }
    public LTAetherWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "aether"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 10);
    }

    @Override
    public int chance() {
        return 50;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(WaterClazz.class, NatureClazz.class, SupernaturalClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
