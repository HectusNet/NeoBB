package net.hectus.neobb.modes.turn.card_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

class CTJackOLantern(data: Block?, cord: Cord?, player: NeoPlayer?) : CardTurn(data, cord, player) {
    override val damage: Double
        get() = if (player?.game?.time == MinecraftTime.MIDNIGHT) 5.0 else 2.0

    override fun item(): ItemStack = ItemStack(Material.JACK_O_LANTERN)
}
