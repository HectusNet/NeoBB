package net.hectus.neobb.game.util;

import net.hectus.neobb.NeoBB;
import net.hectus.neobb.util.Cord;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Range;

public class Arena {
    public final World world;
    public final Cord cornerLow;
    public final Cord cornerHigh;
    private final Block[][][] totalPlacedBlocks = new Block[9][NeoBB.CONFIG.getInt("max-arena-height")][9];
    private Block[][][] placedBlocks;

    public Arena(World world, Cord corner1, Cord corner2) {
        this.world = world;

        Pair<Cord, Cord> corners = Cord.corners(corner1, corner2);
        this.cornerLow = corners.getLeft();
        this.cornerHigh = corners.getRight();
    }

    public Block getBlock(@Range(from = 0, to = 8) int x, int y, @Range(from = 0, to = 8) int z) {
        return totalPlacedBlocks[x][y][z];
    }

    public Block getBlockCurrent(@Range(from = 0, to = 8) int x, int y, @Range(from = 0, to = 8) int z) {
        return placedBlocks[x][y][z];
    }

    public void resetCurrentBlocks() {
        placedBlocks = new Block[0][totalPlacedBlocks[0].length][0];
    }

    public void setBlock(@Range(from = 0, to = 8) int x, int y, @Range(from = 0, to = 8) int z, Block block) {
        totalPlacedBlocks[x][y][z] = block;
        placedBlocks[x][y][z] = block;
    }

    public void setBlock(Location location, Block block) {
        Cord c = Cord.ofLocation(location).subtract(cornerLow);
        setBlock((int) c.x(), (int) c.y(), (int) c.z(), block);
    }
}
