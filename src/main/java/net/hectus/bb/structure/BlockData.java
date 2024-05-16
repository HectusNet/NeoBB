package net.hectus.bb.structure;

import net.hectus.bb.util.Cord;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record BlockData(Material type, Cord relativeCord, BlockFace blockFace) implements Serializable {
    /**
     * Places the block in relation to a structure's starting point.
     * @param world The world in which this block is placed.
     * @param structurePoint The structure's starting point.
     */
    public void place(@NotNull World world, @NotNull Cord structurePoint) {
        Block block = world.getBlockAt(structurePoint.add(relativeCord).toLocation(world));
        block.setType(type, false);
        if (blockFace != null && block.getBlockData() instanceof Directional directional) {
            directional.setFacing(blockFace);
        }
    }

    /**
     * Gets the block face 90 degrees rotated from the current one. Useful for rotating a structure.
     * @return The 90 degrees rotated block face.
     */
    public BlockFace rotateBlockFace() {
        for (BlockFace bf : BlockFace.values()) {
            if (bf.getModX() == blockFace.getModZ() &&
                    bf.getModZ() == -blockFace.getModX() &&
                    bf.getModY() == blockFace.getModY()) {
                return bf;
            }
        }
        return BlockFace.SELF; // Should normally not be reachable.
    }

    /**
     * Converts a {@link Block org.bukkit.block.Block} to a BlockData in relation to a structure's starting point.
     * @param block The block to convert.
     * @param structurePoint The structure's starting point.
     * @return The converted BlockData.
     */
    public static @NotNull BlockData ofBlock(@NotNull Block block, Cord structurePoint) {
        return new BlockData(block.getType(), Cord.ofLocation(block.getLocation()).subtract(structurePoint), block.getBlockData() instanceof Directional directional ? directional.getFacing() : null);
    }
}
