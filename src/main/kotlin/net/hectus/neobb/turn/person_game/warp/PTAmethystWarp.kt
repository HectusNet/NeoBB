package net.hectus.neobb.turn.person_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.StaticStructure
import net.hectus.neobb.structure.StaticStructures

class PTAmethystWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "amethyst", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.AMETHYST

    override val damage: Double = 1.0
    override val chance: Double = 10.0

    override fun apply() {
        player!!.health(player.game.info.startingHealth)
    }
}
