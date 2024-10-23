package net.hectus.neobb.turn.legacy_game.warp;

import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.warp.WarpTurn;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class LWarpTurn extends WarpTurn {
    public LWarpTurn(World world, String name) {
        super(world, "legacy-" + name);
    }

    public LWarpTurn(PlacedStructure data, World world, NeoPlayer player, String name) {
        super(data, world, player, "legacy-" + name);
    }

    public List<ItemStack> items() {
        return super.structureTurnItems(); // Calls StructureTurn#items(), like super.super.items()
    }

    @Override
    public final @NotNull Pair<Material, Material> materials() {
        return Pair.of(Material.BEDROCK, Material.BEDROCK); // This can be fully ignored!
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("legacy-" + name + "-warp");
    }
}
