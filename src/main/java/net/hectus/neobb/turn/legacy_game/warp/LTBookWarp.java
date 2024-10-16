package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.util.Utilities;
import org.bukkit.World;

import java.util.List;

public class LTBookWarp extends LWarpTurn {
    public LTBookWarp(World world) { super(world, "legacy-book"); }
    public LTBookWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "legacy-book"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 35);
    }

    @Override
    public boolean canBePlayed() {
        return Utilities.isWeekday();
    }

    @Override
    public int chance() {
        return 20;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, SupernaturalClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
