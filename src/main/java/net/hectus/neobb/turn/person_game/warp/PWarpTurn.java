package net.hectus.neobb.turn.person_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.warp.WarpTurn;
import net.hectus.neobb.turn.person_game.categorization.WarpCategory;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public abstract class PWarpTurn extends WarpTurn implements WarpCategory {
    public PWarpTurn(World world, String name) {
        super(world, "person-" + name);
    }

    public PWarpTurn(PlacedStructure data, World world, NeoPlayer player, String name) {
        super(data, world, player, "person-" + name);
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("person-" + name + "-warp");
    }

    @Override
    public final @NotNull @Unmodifiable List<Class<? extends Clazz>> allows() {
        return List.of(Clazz.class); // This makes it allow everything all the time.
    }

    @Override
    public final int cost() {
        return 0;
    }
}
