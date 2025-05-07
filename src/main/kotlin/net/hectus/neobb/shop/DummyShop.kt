package net.hectus.neobb.shop

import net.hectus.neobb.player.NeoPlayer

class DummyShop(player: NeoPlayer) : Shop(player) {
    override fun open() {}
}