package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.util.Utilities;
import org.bukkit.World;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class LTSunWarp extends LWarpTurn {
    public LTSunWarp(World world) { super(world, "sun"); }
    public LTSunWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "sun"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Effect(Buff.BuffTarget.OPPONENTS, PotionEffectType.BLINDNESS);

        for (NeoPlayer p : player.game.players()) {
            Turn<?>[] turns = p.inventory.dummyTurnDeck();
            for (int i = 0; i < turns.length; i++) {
                if (turns[i] instanceof WaterClazz)
                    player.inventory.setDeckSlot(i, null, null);
            }
        }
    }

    @Override
    public boolean canBePlayed() {
        return Utilities.isDaytime();
    }

    @Override
    public int chance() {
        return 30;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(NeutralClazz.class, HotClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
