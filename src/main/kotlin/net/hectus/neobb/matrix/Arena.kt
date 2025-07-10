package net.hectus.neobb.matrix

import com.marcpg.libpg.util.toCord
import com.marcpg.libpg.util.toLocation
import net.hectus.neobb.game.Game
import net.hectus.neobb.util.Configuration
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity

class Arena(val game: Game) {
    val completeSpace: BlockSpace = BlockSpace(y = Configuration.MAX_ARENA_HEIGHT)
    val placedSpace: BlockSpace = BlockSpace(y = Configuration.MAX_ARENA_HEIGHT)
    var placedBlocks = 0
        private set

    operator fun set(x: Int, y: Int, z: Int, block: BlockInfo) {
        completeSpace[x, y, z] = block
        placedSpace[x, y, z] = block
    }

    fun addBlock(block: Block) {
        val cord = block.location.toCord() - game.warp.lowCorner
        this[cord.x.toInt(), cord.y.toInt(), cord.z.toInt()] = BlockInfo(cord, block.type)
        placedBlocks++
    }

    fun clear() {
        completeSpace.forEach {
            game.playedWarps.forEach { w ->
                (w.lowCorner + it!!.cord).toLocation(game.world).block.type = Material.AIR
            }
        }
        game.history.forEach { t ->
            if (t.data is Entity)
                t.data.remove()
        }
    }

    fun resetCurrentBlocks() {
        placedSpace.clear()
        placedBlocks = 0
    }
}
