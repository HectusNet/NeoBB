package net.hectus.neobb.shop

import net.hectus.neobb.player.NeoPlayer

class RandomizedShop(player: NeoPlayer) : Shop(player) {
    override fun open() {
        player.inventory.fillInRandomly()
        done()
    }
}
