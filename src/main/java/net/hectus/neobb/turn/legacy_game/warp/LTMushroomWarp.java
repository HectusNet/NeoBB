package net.hectus.neobb.turn.legacy_game.warp;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.shop.Shop;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.World;

import java.util.List;

public class LTMushroomWarp extends LWarpTurn {
    public LTMushroomWarp(World world) { super(world, "legacy-mushroom"); }
    public LTMushroomWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "legacy-mushroom"); }

    @Override
    public void apply() {
        super.apply();
        Turn<?> turn = Shop.turn(Randomizer.fromCollection(player.shop.turns), player);
        turn.items().forEach(item -> player.inventory.addToDeck(item, turn));
    }

    @Override
    public int chance() {
        return 60;
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
