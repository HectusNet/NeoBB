package net.hectus.neobb.modes.turn.card_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.material
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

abstract class CardTurn(data: Block?, cord: Cord?, player: NeoPlayer?) : Turn<Block>(data, cord?.plus(Cord(0.5, 0.5, 0.5)), player) {
    abstract override val damage: Double

    override fun item(): ItemStack = ItemStack(this::class.material())

    // Prevent the suggestion system from doing anything:
    override fun goodChoice(player: NeoPlayer): Boolean = false
}

class CTChest(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    override val damage: Double = 5.0
}

class CTDaylightDetector(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    override val damage: Double = 3.0
}

class CTFlowerPot(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    override val damage: Double = 2.0
}

class CTJackOLantern(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    override val damage: Double
        get() = if (player?.game?.time == MinecraftTime.MIDNIGHT) 5.0 else 2.0
    override fun item(): ItemStack = ItemStack(Material.JACK_O_LANTERN)
}

class CTOakTrapdoor(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    override val damage: Double = 8.0
}

class CTPointedDripstone(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    override val damage: Double = 6.0
}

class CTRedstoneLamp(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    override val damage: Double = 5.0
}

class CTTorch(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    override val damage: Double = 6.0
}

class CTWaxedExposedCutCopperStairs(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    override val damage: Double = 5.0
}
