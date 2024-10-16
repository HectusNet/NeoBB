package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import net.hectus.neobb.turn.default_game.throwable.TEnderPearl;
import org.bukkit.World;

import java.util.List;

public class LTEndWarp extends LWarpTurn {
    public LTEndWarp(World world) { super(world, "legacy-end"); }
    public LTEndWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "legacy-end"); }

    @Override
    public void apply() {
        super.apply();
        new Buff.Luck(Buff.BuffTarget.YOU, 10);

        TEnderPearl pearl = new TEnderPearl(player);
        player.inventory.addToDeck(pearl.item(), pearl);
    }

    @Override
    public int chance() {
        return 60;
    }

    @Override
    public List<Class<? extends Clazz>> allows() {
        return List.of(ColdClazz.class, WaterClazz.class, RedstoneClazz.class, SupernaturalClazz.class);
    }

    @Override
    public int cost() {
        return 4;
    }
}
