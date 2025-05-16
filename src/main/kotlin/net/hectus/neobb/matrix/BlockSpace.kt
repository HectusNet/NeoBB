package net.hectus.neobb.matrix

import com.marcpg.libpg.storing.Cord
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bukkit.Material

@Serializable
data class BlockInfo(val cord: @Contextual Cord, val material: Material): java.io.Serializable

@Serializable
data class BlockSpace(val x: Int = 9, val y: Int = 9, val z: Int = 9) {
    private val blocks: Array<Array<Array<BlockInfo?>>> = Array(x) { Array(y) { Array(z) { null } } }

    operator fun get(x: Int, y: Int, z: Int): BlockInfo? = blocks[x][y][z]

    operator fun set(x: Int, y: Int, z: Int, block: BlockInfo?) {
        blocks[x][y][z] = block
    }

    fun forEach(allowNull: Boolean = false, action: (block: BlockInfo?) -> Unit) {
        for (i in 0 until x) {
            for (j in 0 until y) {
                for (k in 0 until z) {
                    val block = blocks[i][j][k] ?: if (!allowNull) continue else null
                    action.invoke(block)
                }
            }
        }
    }

    fun forEach(allowNull: Boolean = false, action: (block: BlockInfo?, x: Int, y: Int, z: Int) -> Unit) {
        for (i in 0 until x) {
            for (j in 0 until y) {
                for (k in 0 until z) {
                    val block = blocks[i][j][k] ?: if (!allowNull) continue else null
                    action.invoke(block, x, y, z)
                }
            }
        }
    }

    fun materials(): MutableMap<Material, Int> {
        val map: MutableMap<Material, Int> = mutableMapOf()
        forEach { block ->
            map[block!!.material] = (map[block.material] ?: 0) + 1
        }
        return map
    }

    fun clear() {
        for (i in 0 until x)
            for (j in 0 until y)
                for (k in 0 until z)
                    blocks[i][j][k] = null
    }
}
