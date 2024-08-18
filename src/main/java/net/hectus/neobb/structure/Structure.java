package net.hectus.neobb.structure;

import net.hectus.neobb.game.util.Arena;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Structure implements Serializable {
    public final String name;
    public final Set<Material> materials = new HashSet<>();
    public final BlockInfo[][][] blocks;
    public final int blocksX;
    public final int blocksY;
    public final int blocksZ;

    public Structure(String name, BlockInfo @NotNull [][][] blocks) {
        this.name = name;
        this.blocks = blocks;
        this.blocksX = blocks.length;
        this.blocksY = blocks[0].length;
        this.blocksZ = blocks[0][0].length;

        materials.addAll(Arrays.stream(blocks).parallel() // I'm actually not sure if bukkit supports parallel streams, but it shouldn't cause any issues even if it doesn't.
                .flatMap(Arrays::stream)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .map(BlockInfo::material)
                .collect(Collectors.toSet()));
    }

    public boolean isInArena(@NotNull Arena arena) {
        return matchMaterials(arena.currentPlacedMaterials(), materials) && isInRegion(arena.currentPlacedBlocks());
    }

    public boolean matchMaterials(Collection<Material> reference, @NotNull Collection<Material> comparison) {
        for (Material material : comparison) {
            if (!reference.contains(material))
                return false;
        }
        return true;
    }

    public boolean isInRegion(BlockInfo @NotNull [][][] region) {
        int worldX = region.length;
        int worldY = region[0].length;
        int worldZ = region[0][0].length;

        for (int startX = 0; startX <= worldX - blocksX; startX++) {
            for (int startY = 0; startY <= worldY - blocksY; startY++) {
                for (int startZ = 0; startZ <= worldZ - blocksZ; startZ++) {
                    if (matchesStructure(region, startX, startY, startZ)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean matchesStructure(BlockInfo[][][] region, int startX, int startY, int startZ) {
        for (int x = 0; x < blocksX; x++) {
            for (int y = 0; y < blocksY; y++) {
                for (int z = 0; z < blocksZ; z++) {
                    BlockInfo worldBlock = region[startX + x][startY + y][startZ + z];
                    BlockInfo structureBlock = blocks[x][y][z];

                    if (structureBlock != null && !structureBlock.equals(worldBlock))
                        return false;
                }
            }
        }
        return true;
    }
}
