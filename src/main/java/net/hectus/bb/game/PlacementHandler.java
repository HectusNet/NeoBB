package net.hectus.bb.game;

import net.hectus.bb.BlockBattles;
import net.hectus.bb.event.GameEvents;
import net.hectus.bb.structure.Structure;
import net.hectus.bb.structure.StructureCalculator;
import net.hectus.bb.structure.StructureManager;
import net.hectus.bb.turn.Turn;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlacementHandler {
    public static final List<Material> STRUCTURE_BLOCKS = StructureManager.loadedStructures().stream()
            .flatMap(structure -> structure.materialWeights().keySet().stream())
            .toList();

    private final List<Block> placedBlocks = new ArrayList<>();

    public void add(Block block, Player player) {
        placedBlocks.add(block);
        if (placedBlocks.size() == 1) {
            Bukkit.getScheduler().runTaskLater(BlockBattles.getPlugin(BlockBattles.class), () -> {
                if (placedBlocks.size() == 1) { // No other block has been placed.
                    for (Turn turn : Turn.values()) {
                        if (turn.type == Turn.ItemType.BLOCK && (turn.name().equals(block.getType().name()) || turn.materials().contains(block.getType()))) {
                            GameEvents.turn(player, Turn.valueOf(block.getType().name()), block, null);
                            break;
                        }
                    }
                }
            }, 30);
        } else {
            Map<Structure, Double> results = StructureCalculator.predict(Structure.ofBlocks(placedBlocks, "placed"));
            Map.Entry<Structure, Double> result = results.entrySet().iterator().next();
            player.sendActionBar(Component.text(result.getKey().name() + " (" + (result.getValue() * 100) + "%)"));
            if (result.getValue() >= 1.0)
                GameEvents.turn(player, Turn.valueOf(result.getKey().name().toUpperCase()), null, null);
        }
    }

    public void reset() {
        placedBlocks.clear();
    }

    private boolean requireWaiting(Block block, Player player) {
        return STRUCTURE_BLOCKS.contains(block.getType());
    }
}
