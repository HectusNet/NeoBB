package net.hectus.neobb.modes.shop

import com.marcpg.libpg.util.bukkitRunLater
import net.hectus.neobb.player.NeoPlayer

class RandomizedShop(player: NeoPlayer) : Shop(player) {
    override fun open() {
        bukkitRunLater(10) {
            player.inventory.fillInRandomly()
            done()
        }
    }
}
