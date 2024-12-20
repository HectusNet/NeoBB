package net.hectus.neobb.structure;

import com.google.gson.GsonBuilder;
import com.marcpg.libpg.storing.Cord;
import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.util.Arena;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Structure implements Serializable {
    public final String name;
    public final Set<Material> materials = new HashSet<>();
    public final BlockInfo[][][] blocks;
    public final int blocksX;
    public final int blocksY;
    public final int blocksZ;
    public transient Structure rotated;

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

    public Structure(String name, World world, @NotNull Cord corner1, @NotNull Cord corner2) {
        this(name, blocks(world, corner1, corner2));
    }

    public void save() {
        String mode = Objects.requireNonNullElse(NeoBB.CONFIG.getString("structure-mode"), "local");
        if (mode.equals("server")) {
            NeoBB.LOG.warn("Saving structures to the server is not supported yet!");
        } else {
            File file = NeoBB.STRUCTURE_DIR.resolve(name + ".json").toFile();
            try (FileWriter writer = new FileWriter(file)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(this, writer);
                NeoBB.LOG.info("Saved structure {} to {}", name, file.getAbsolutePath());
            } catch (IOException e) {
                NeoBB.LOG.error("Failed to save structure {} to {}", name, file.getAbsolutePath(), e);
            }
        }
    }

    public void remove() throws IOException {
        String mode = Objects.requireNonNullElse(NeoBB.CONFIG.getString("structure-mode"), "local");
        if (mode.equals("server")) {
            NeoBB.LOG.warn("Removing structures from the server is not supported yet!");
        } else {
            Files.delete(NeoBB.STRUCTURE_DIR.resolve(name + ".json"));
            NeoBB.LOG.info("Removed structure {}", name);
        }
    }

    public Structure rotated() {
        if (rotated != null) return rotated;

        BlockInfo[][][] newBlocks = new BlockInfo[blocksZ][blocksY][blocksX];
        for (BlockInfo oldBlock : Arrays.stream(blocks).parallel().flatMap(Arrays::stream).flatMap(Arrays::stream).filter(Objects::nonNull).toList()) {
            Cord cord = oldBlock.cord().rotated();
            newBlocks[(int) cord.x()][(int) cord.y()][(int) cord.z()] = new BlockInfo(cord, oldBlock.material());
        }
        rotated = new Structure(name, newBlocks);
        return rotated;
    }

    public void place(Location location) {
        Utilities.loop(blocks, false, block -> {
            Cord cord = block.cord();
            location.clone().add(cord.x(), cord.y(), cord.z()).getBlock().setType(block.material());
        });
    }

    public boolean isInArena(@NotNull Arena arena) {
        return matchMaterials(arena.currentPlacedMaterials(), materials) && isInRegion(arena.currentPlacedBlocks(), arena.currentPlacedBlocksAmount());
    }

    public boolean matchMaterials(@NotNull Collection<Material> reference, @NotNull Collection<Material> comparison) {
        for (Material material : reference) {
            if (!comparison.contains(material))
                return false;
        }
        return true;
    }

    public boolean isInRegion(BlockInfo @NotNull [][][] region, int regionBlockAmount) {
        if (countElements(blocks) != regionBlockAmount) return false;
        for (int startX = 0; startX <= region.length - blocksX; startX++) {
            for (int startY = 0; startY <= region[0].length - blocksY; startY++) {
                for (int startZ = 0; startZ <= region[0][0].length - blocksZ; startZ++) {
                    if (matchesAtPosition(region, startX, startY, startZ))
                        return true;
                }
            }
        }
        return false;
    }

    private boolean matchesAtPosition(BlockInfo[][][] region, int offsetX, int offsetY, int offsetZ) {
        for (int x = 0; x < blocksX; x++) {
            for (int y = 0; y < blocksY; y++) {
                for (int z = 0; z < blocksZ; z++) {
                    BlockInfo structureBlock = blocks[x][y][z];
                    if (structureBlock != null) {
                        BlockInfo regionBlock = region[offsetX + x][offsetY + y][offsetZ + z];
                        if (regionBlock == null || !regionBlock.material().equals(structureBlock.material()))
                            return false;
                    }
                }
            }
        }
        return true;
    }

    public static int countElements(Object @NotNull [][][] array) {
        int count = 0;
        for (Object[][] x : array) {
            for (Object[] y : x) {
                for (Object z : y) {
                    if (z != null)
                        count++;
                }
            }
        }
        return count;
    }

    public static BlockInfo @NotNull [][][] blocks(World world, @NotNull Cord corner1, @NotNull Cord corner2) {
        Pair<Cord, Cord> corners = Cord.corners(corner1, corner2);
        Cord low = corners.left(), high = corners.right();

        BlockInfo[][][] blocks = new BlockInfo[(int) (high.x() - low.x() + 1)][(int) (high.y() - low.y() + 1)][(int) (high.z() - low.z() + 1)];
        for (int x = (int) low.x(); x <= high.x(); x++) {
            for (int y = (int) low.y(); y <= high.y(); y++) {
                for (int z = (int) low.z(); z <= high.z(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType().isAir()) continue;
                    blocks[x - (int) low.x()][y - (int) low.y()][z - (int) low.z()] = new BlockInfo(new Cord(x - (int) low.x(), y - (int) low.y(), z - (int) low.z()), block.getType());
                }
            }
        }
        return blocks;
    }
}
