package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.World;

import java.util.List;

public class LTCliffWarp extends LWarpTurn {
    public LTCliffWarp(World world) { super(world, "legacy-cliff"); }
    public LTCliffWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "legacy-cliff"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 15);
    }

    @Override
    public int chance() {
        return 67;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, ColdClazz.class, WaterClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
