package net.hectus.neobb.modes.shop

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.bukkitRunLater

class RandomizedShop(player: NeoPlayer) : Shop(player) {
    override fun open() {
        bukkitRunLater(10) {
            player.inventory.fillInRandomly()
            done()
        }
    }
}
