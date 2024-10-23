package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.World;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class LTVoidWarp extends LWarpTurn {
    public LTVoidWarp(World world) { super(world, "void"); }
    public LTVoidWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "void"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 15);
        new Buff.Effect(Buff.BuffTarget.OPPONENTS, PotionEffectType.BLINDNESS);
        player.opponents(true).forEach(p -> p.addModifier(Modifiers.P_NO_JUMP));
    }

    @Override
    public int chance() {
        return 30;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, ColdClazz.class, RedstoneClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
