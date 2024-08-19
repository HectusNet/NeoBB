package net.hectus.neobb.game.util;

import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.structure.BlockInfo;
import net.hectus.neobb.util.Cord;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Set;

public class Arena {
    public final Game game;
    public final World world;

    private final BlockInfo[][][] totalPlacedBlocks = new BlockInfo[9][NeoBB.CONFIG.getInt("max-arena-height")][9];

    private final Set<Material> placedMaterials = new HashSet<>();
    private BlockInfo[][][] placedBlocks = new BlockInfo[9][NeoBB.CONFIG.getInt("max-arena-height")][9];

    public Arena(Game game, World world) {
        this.game = game;
        this.world = world;
    }

    public BlockInfo getBlock(@Range(from = 0, to = 8) int x, int y, @Range(from = 0, to = 8) int z) {
        return totalPlacedBlocks[x][y][z];
    }

    public BlockInfo getBlockCurrent(@Range(from = 0, to = 8) int x, int y, @Range(from = 0, to = 8) int z) {
        return placedBlocks[x][y][z];
    }

    public int currentPlacedBlocksAmount() { // TODO: Improve this!
        int blocks = 0;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                for (int z = 0; z < 9; z++) {
                    if (placedBlocks[x][y][z] != null)
                        blocks++;
                }
            }
        }
        return blocks;
    }

    /**
     * Do not directly modify the objects in this array. Instead use the methods below!
     * @return The currently placed blocks of the current turn.
     * @see #resetCurrentBlocks()
     * @see #setBlock(int, int, int, BlockInfo)
     * @see #addBlock(Block)
     */
    public BlockInfo[][][] currentPlacedBlocks() {
        return placedBlocks;
    }

    public @Unmodifiable Set<Material> currentPlacedMaterials() {
        return placedMaterials;
    }

    public void resetCurrentBlocks() {
        placedBlocks = new BlockInfo[9][totalPlacedBlocks[0].length][9];
        placedMaterials.clear();
    }

    public void setBlock(@Range(from = 0, to = 8) int x, int y, @Range(from = 0, to = 8) int z, BlockInfo block) {
        totalPlacedBlocks[x][y][z] = block;
        placedBlocks[x][y][z] = block;
    }

    public void addBlock(@NotNull Block block) {
        Cord c = Cord.ofLocation(block.getLocation().clone().subtract(game.warp().lowCorner()));
        setBlock((int) c.x(), (int) c.y(), (int) c.z(), new BlockInfo(c, block.getType()));
    }
}
