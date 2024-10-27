package net.hectus.neobb.turn.legacy_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.warp.WarpTurn;
import org.bukkit.World;

public abstract class LWarpTurn extends WarpTurn {
    public LWarpTurn(World world, String name) {
        super(world, "legacy-" + name);
    }

    public LWarpTurn(PlacedStructure data, World world, NeoPlayer player, String name) {
        super(data, world, player, "legacy-" + name);
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("legacy-" + name + "-warp");
    }
}
