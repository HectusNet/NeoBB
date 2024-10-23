package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import org.bukkit.World;

import java.util.List;

public class LTAmethystWarp extends LWarpTurn {
    public LTAmethystWarp(World world) { super(world, "amethyst"); }
    public LTAmethystWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "amethyst"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 20);
    }

    @Override
    public int chance() {
        return 30;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, ColdClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
