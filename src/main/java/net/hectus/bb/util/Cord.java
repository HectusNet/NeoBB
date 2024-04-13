package net.hectus.bb.util;

import com.marcpg.libpg.storing.Pair;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record Cord(double x, double y, double z) implements Serializable {
    /**
     * Converts this current Cord to a {@link Location org.bukkit.Location}.
     * @param world The world of the converted Location.
     * @return The converted Location.
     */
    public @NotNull Location toLocation(@NotNull World world) {
        return new Location(world, x, y, z);
    }

    /**
     * Rotates the values of this coordinate. Does not affect this object and creates a new one for the result.
     * It basically makes a new cord with x and z being switched around, which is useful for rotating structures.
     * @return The result of this operation as a new Cord.
     */
    public @NotNull Cord rotateValues() {
        return new Cord(z, y, x);
    }

    /**
     * Subtracts the input cord from this cord. Does not affect this object and creates a new one for the result.
     * @param cord The subtrahend of this subtraction.
     * @return The result/difference of this subtraction as a new Cord.
     */
    public @NotNull Cord subtract(@NotNull Cord cord) {
        return new Cord(x - cord.x, y - cord.y, z - cord.z);
    }

    /**
     * Adds the input cord to this cord. Does not affect this object and creates a new one for the result.
     * @param cord The second addend of this addition.
     * @return The sum/total of this addition as a new Cord.
     */
    public @NotNull Cord add(@NotNull Cord cord) {
        return new Cord(x + cord.x, y + cord.y, z + cord.z);
    }

    /**
     * Multiplies the input cord with this cord. Does not affect this object and creates a new one for the result.
     * @param cord The second factor of this multiplication.
     * @return The result/product of this multiplication as a new Cord.
     */
    public @NotNull Cord multiply(@NotNull Cord cord) {
        return new Cord(x * cord.x, y * cord.y, z * cord.z);
    }

    /**
     * Converts a {@link Location org.bukkit.Location} to a simple {@link Cord}.
     * @param location The Location to convert.
     * @return The converted Cord.
     */
    public static @NotNull Cord ofLocation(@NotNull Location location) {
        return new Cord(location.getX(), location.getY(), location.getZ());
    }

    /**
     * Converts two corners, so one has the lowest x, y and z and one has the highest x, y and z.
     * @param corner1 The first corner.
     * @param corner2 The second corner.
     * @return A pair where the left side has the lower and the right the higher x, y and z. <br>
     *         {@code Pair<Lowest XYZ, Highest XYZ>}
     */
    public static @NotNull Pair<Cord, Cord> corners(@NotNull Cord corner1, @NotNull Cord corner2) {
        return Pair.of(
                new Cord(Math.min(corner1.x(), corner2.x()), Math.min(corner1.y(), corner2.y()), Math.min(corner1.z(), corner2.z())),
                new Cord(Math.max(corner1.x(), corner2.x()), Math.max(corner1.y(), corner2.y()), Math.max(corner1.z(), corner2.z()))
        );
    }
}
