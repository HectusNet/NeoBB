package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import org.bukkit.World;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class LTDesertWarp extends LWarpTurn {
    public LTDesertWarp(World world) { super(world, "legacy-desert"); }
    public LTDesertWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "legacy-desert"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 5);
        new Buff.Effect(Buff.BuffTarget.OPPONENTS, PotionEffectType.SLOWNESS, 1);
    }

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
