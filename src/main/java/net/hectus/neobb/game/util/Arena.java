package net.hectus.neobb.game.util;

import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.structure.BlockInfo;
import net.hectus.neobb.util.Cord;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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

    public void cleanUp() {
        Utilities.loop(totalPlacedBlocks, false, b -> game.warp().lowCorner().add(b.cord()).toLocation(world).getBlock().setType(Material.AIR));
        game.history().forEach(t -> {
            if (t.data() instanceof Entity entity)
                entity.remove();
        });
    }

    public int currentPlacedBlocksAmount() {
        AtomicInteger blocks = new AtomicInteger();
        Utilities.loop(placedBlocks, false, block -> blocks.incrementAndGet());
        return blocks.get();
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
        Cord c = Cord.ofLocation(block.getLocation().clone().subtract(game.warp().location()));
        setBlock((int) c.x(), (int) c.y(), (int) c.z(), new BlockInfo(c, block.getType()));
    }
}
