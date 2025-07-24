package net.hectus.neobb.matrix.structure

import com.marcpg.libpg.storing.Cord
import com.marcpg.storage.JsonUtils
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.hectus.neobb.NeoBB
import net.hectus.neobb.matrix.BlockInfo
import net.hectus.neobb.matrix.BlockSpace
import net.hectus.neobb.util.Configuration
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.roundToInt

data class PlacedStructure(val structure: Structure, val lastBlock: Block)

@Serializable
open class Structure(
    val name: String,
    val materials: MutableMap<Material, Int> = mutableMapOf(),
    val blocks: BlockSpace = BlockSpace(y = Configuration.MAX_ARENA_HEIGHT),
    @Transient @kotlin.jvm.Transient var rotated: Structure? = null
): java.io.Serializable {
    companion object {
        private fun blocks(world: World, corner1: Cord, corner2: Cord): BlockSpace {
            val corners = Cord.corners(corner1, corner2)
            val low = corners.first
            val high = corners.second

            val blocks = BlockSpace(
                high.x.toInt() - low.x.toInt() + 1,
                high.y.toInt() - low.y.toInt() + 1,
                high.z.toInt() - low.z.toInt() + 1
            )
            for (x in low.x.toInt()..high.x.toInt()) {
                for (y in low.y.toInt()..high.y.toInt()) {
                    for (z in low.z.toInt()..high.z.toInt()) {
                        val block = world.getBlockAt(x, y, z)
                        if (block.type.isAir) continue

                        val cord = Cord(x - low.x, y - low.y, z - low.z)
                        blocks[cord.x.toInt(), cord.y.toInt(), cord.z.toInt()] = BlockInfo(cord, block.type)
                    }
                }
            }
            return blocks
        }
    }

    constructor(name: String, blocks: BlockSpace): this(name, blocks.materials(), blocks, null)
    constructor(name: String, world: World, corner1: Cord, corner2: Cord): this(name, blocks(world, corner1, corner2))

    fun save() {
        if (Configuration.STRUCTURE_MODE == Configuration.StructureMode.SERVER) {
            NeoBB.LOG.warn("Saving structures to the server is not yet supported yet.")
        } else {
            val file: Path = Configuration.STRUCTURE_MODE.pathSupplier.invoke().resolve("$name.json")
            runCatching {
                JsonUtils.save(this, file.toFile())
                NeoBB.LOG.info("Saved structure $name to $file.")
            }.onFailure { e ->
                NeoBB.LOG.error("Failed to save structure $name to $file.", e)
            }
        }
    }

    fun remove() {
        if (Configuration.STRUCTURE_MODE == Configuration.StructureMode.SERVER) {
            NeoBB.LOG.warn("Removing structures from the server is not yet supported yet.")
        } else {
            val file: Path = Configuration.STRUCTURE_MODE.pathSupplier.invoke().resolve("$name.json")
            runCatching {
                Files.delete(file)
                NeoBB.LOG.info("Deleted structure $name from $file.")
            }.onFailure { e ->
                NeoBB.LOG.error("Failed to delete structure $name from $file.", e)
            }
        }
    }

    fun rotated(): Structure {
        if (rotated != null) return rotated!!

        val xLength = blocks.x
        val zLength = blocks.z
        val newBlocks = BlockSpace(zLength, blocks.y, xLength)

        for (y in 0 until blocks.y)
            for (x in 0 until xLength)
                for (z in 0 until zLength)
                    newBlocks[z, y, xLength - 1 - x] = blocks[x, y, z]

        val structure = Structure(name, materials, newBlocks, this)
        rotated = structure
        return structure
    }

    fun place(location: Location, replace: Boolean): Int {
        var total = 0.0
        var obstructed = 0.0
        blocks.forEach { block ->
            total++
            val realBlock: Block = location.clone().add(block!!.cord.x, block.cord.y, block.cord.z).block
            if (realBlock.isEmpty || replace)
                realBlock.type = block.material
            else obstructed++
        }
        return ((obstructed / total) * 100).roundToInt()
    }

    fun isInSpace(space: BlockSpace): Boolean {
        if (materials != space.materials()) return false

        for (startX in 0..space.x - blocks.x) {
            for (startY in 0..space.y - blocks.y) {
                for (startZ in 0..space.z - blocks.z) {
                    if (matchesAtPosition(space, startX, startY, startZ)) return true
                }
            }
        }
        return false
    }

    fun items(): List<ItemStack> = materials.entries.map { ItemStack(it.key, it.value) }

    private fun matchesAtPosition(space: BlockSpace, offsetX: Int, offsetY: Int, offsetZ: Int): Boolean {
        for (x in 0..<blocks.x) {
            for (y in 0..<blocks.y) {
                for (z in 0..<blocks.z) {
                    val structureBlock = blocks[x, y, z] ?: continue

                    val spaceBlock = space[offsetX + x, offsetY + y, offsetZ + z]
                    if (spaceBlock == null || spaceBlock.material != structureBlock.material)
                        return false
                }
            }
        }
        return true
    }
}
