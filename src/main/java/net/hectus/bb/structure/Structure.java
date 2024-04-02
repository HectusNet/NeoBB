package net.hectus.bb.structure;

import com.marcpg.libpg.storing.Pair;
import net.hectus.bb.util.Cord;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Structure implements Serializable {
    @Serial private static final long serialVersionUID = 8815022013296354560L;

    private final String name;
    private final List<BlockData> data = new ArrayList<>();
    private final Map<Material, Integer> materialWeights = new HashMap<>();

    /**
     * Creates a new Structure based on its name.
     * @param name The Structure's name.
     */
    public Structure(String name) {
        this.name = name;
    }

    /**
     * Gets the Structure's name.
     * @return The Structure's name.
     */
    public String name() {
        return name;
    }

    /**
     * Rotates the Structure by 90 degrees, which is done by switching the x and z cords of all block data.
     * @return The rotated Structure as a new object.
     */
    public Structure rotated() {
        Structure rotated = new Structure(name);
        rotated.materialWeights.putAll(materialWeights);
        for (BlockData blockData : data) {
            rotated.data.add(new BlockData(blockData.type(), blockData.relativeCord().rotateValues(), blockData.rotateBlockFace()));
        }
        return rotated;
    }

    public void place(World world, Cord structurePoint) {
        for (BlockData blockData : data)
            blockData.place(world, structurePoint);
    }

    /**
     * Saves a specific region of the world as a Structure, by going though all blocks,
     * converting them to {@link BlockData} and then saving the structure into memory.
     * Use {@link StructureManager#save()} afterwards to also save it as a file.
     * @param world The world in which the Structure stands.
     * @param corner1 The first corner of the structure.
     * @param corner2 The second corner of the structure.
     * @param name The name/identifier of the structure, which should be unique.
     * @return The loaded Structure.
     */
    public static @NotNull Structure save(World world, Location corner1, Location corner2, @Pattern("^[a-z0-9_-]+$") String name) {
        Structure structure = new Structure(name);

        Pair<Cord, Cord> lowestAndHighestCord = Cord.corners(Cord.ofLocation(corner1), Cord.ofLocation(corner2));
        Cord lowest = lowestAndHighestCord.left();
        Cord highest = lowestAndHighestCord.right();

        for (double x = lowest.x(); x <= highest.x(); x++) {
            for (double y = lowest.y(); y <= highest.y(); y++) {
                for (double z = lowest.z(); z <= highest.z(); z++) {
                    Block block = world.getBlockAt((int) x, (int) y, (int) z);
                    Material type = block.getType();
                    if (!type.isAir()) {
                        structure.data.add(BlockData.ofBlock(block, lowest));
                        structure.materialWeights.put(type, structure.materialWeights.getOrDefault(type, 0) + 1);
                    }
                }
            }
        }
        return structure;
    }
}
