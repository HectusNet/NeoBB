package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.BlockInfo;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.function.WarpFunction;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.hectus.neobb.util.Cord;
import net.hectus.neobb.util.Modifiers;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class WarpTurn extends StructureTurn implements WarpFunction {
    public final Location center;
    public final String name;

    public WarpTurn(World world, String name) {
        super(null);
        this.location = Utilities.listToLocation(world, NeoBB.CONFIG.getIntegerList("warps." + name));
        this.name = name;
        this.center = location().add(5, 0, 5);
    }

    public WarpTurn(PlacedStructure data, World world, NeoPlayer player, String name) {
        super(data, player);
        this.location = Utilities.listToLocation(world, NeoBB.CONFIG.getIntegerList("warps." + name));
        this.name = name;
        this.center = location().add(5, 0, 5);
    }

    public abstract int chance();
    public abstract List<Class<? extends Clazz>> allows();

    /**
     * @return {@code Pair<Main, Center>}
     */
    public abstract Pair<Material, Material> materials();

    @Override
    public List<ItemStack> items() {
        return List.of(new ItemStack(materials().left(), 4), new ItemStack(materials().right()));
    }

    public enum Temperature { COLD, NORMAL, HOT }
    public Temperature temperature() {
        return switch (allows().getFirst().getSimpleName()) {
            case "ColdClazz" -> Temperature.COLD;
            case "HotClazz" -> Temperature.HOT;
            default -> Temperature.NORMAL;
        };
    }

    public Cord lowCorner() {
        return Cord.ofLocation(location());
    }

    public Cord highCorner() {
        return lowCorner().add(new Cord(9, NeoBB.CONFIG.getInt("max-arena-height"), 9));
    }

    @Override
    public boolean requiresUsageGuide() {
        return true;
    }

    @Override
    public int maxAmount() {
        return 1;
    }

    @Override
    public void apply() {
        if (player.hasModifier(Modifiers.P_DEFAULT_100P_WARP) || Randomizer.boolByChance(chance())) {
            player.removeModifier(Modifiers.P_DEFAULT_100P_WARP);
            WarpTurn oldWarp = player.game.warp();
            player.game.players().forEach(p -> p.player.teleport(p.player.getLocation().clone().subtract(oldWarp.location()).add(location())));
            player.game.warp(this);
        }
    }

    @Override
    public Structure referenceStructure() {
        Pair<Material, Material> materials = materials();
        return new Structure(name + "-warp", new BlockInfo[][][] {
                {
                        {
                                new BlockInfo(new Cord(0, 0, 1), materials.left())
                        },
                        {
                                new BlockInfo(new Cord(1, 0, 0), materials.left()),
                                new BlockInfo(new Cord(1, 0, 1), materials.right()),
                                new BlockInfo(new Cord(1, 0, 2), materials.left())
                        },
                        {
                                new BlockInfo(new Cord(2, 0, 1), materials.left())
                        }
                }
        });
    }
}
