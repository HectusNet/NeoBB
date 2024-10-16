package net.hectus.neobb.turn.default_game.structure;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class StructureTurn extends Turn<PlacedStructure> {
    public StructureTurn(NeoPlayer player) { super(null, null, player); }
    public StructureTurn(PlacedStructure data, NeoPlayer player) { super(data, data.lastBlock().getLocation(), player); }

    public abstract Structure referenceStructure();

    public List<ItemStack> items() {
        return extractItems(referenceStructure());
    }

    public static List<ItemStack> extractItems(Structure structure) {
        if (structure == null) return List.of();

        Map<Material, AtomicInteger> materials = new HashMap<>();
        Utilities.loop(structure.blocks, false, block -> {
            if (block == null) return;
            if (materials.containsKey(block.material())) {
                materials.get(block.material()).incrementAndGet();
            } else {
                materials.put(block.material(), new AtomicInteger(1));
            }
        });
        return materials.entrySet().stream().map(e -> new ItemStack(e.getKey(), e.getValue().get())).toList();
    }
}
