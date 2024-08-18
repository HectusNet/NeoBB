package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Pair;
import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.BlockInfo;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.function.WarpFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.StructureUsage;
import net.hectus.neobb.util.Cord;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Warp extends Turn<PlacedStructure> implements StructureUsage, WarpFunction {
    public final Location center;
    public final String name;

    public Warp(NeoPlayer player, String name) {
        super(null, Utilities.listToLocation(player.game.world(), NeoBB.CONFIG.getIntegerList("warps." + name)), player);
        this.name = name;
        this.center = location().add(5, 5, 5);
    }

    public Warp(PlacedStructure data, NeoPlayer player, String name) {
        super(data, Utilities.listToLocation(player.game.world(), NeoBB.CONFIG.getIntegerList("warps." + name)), player);
        this.name = name;
        this.center = location().add(5, 5, 5);
    }

    public abstract int chance();
    public abstract List<Class<? extends Clazz>> allows();

    /**
     * @return {@code Pair<Main, Center>}
     */
    public abstract Pair<Material, Material> materials();

    @Override
    public ItemStack item() {
        return new ItemStack(materials().left(), 4); // TODO: Add the second item!
    }

    public Location lowCorner() {
        return location;
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
        if (Randomizer.boolByChance(chance())) {
            Warp oldWarp = player.game.warp();
            player.game.players().forEach(p ->p.player.teleport(p.player.getLocation().clone().subtract(oldWarp.lowCorner()).add(lowCorner())));
            player.game.warp(this);
        }
    }

    @Override
    public PlacedStructure getValue() {
        return data;
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
