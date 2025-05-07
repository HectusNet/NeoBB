package net.hectus.neobb.turn.person_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure

class PTAmethystWarp(data: PlacedStructure?, player: NeoPlayer?) : PWarpTurn(data, "amethyst", player) {
    override val damage: Double = 1.0
    override val chance: Double = 10.0

    override fun apply() {
        player!!.health(player.game.info.startingHealth)
    }
}