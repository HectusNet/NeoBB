package net.hectus.neobb.modes.turn.person_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.player.NeoPlayer

class PTAmethystWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "amethyst", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.AMETHYST

    override val damage: Double = 1.0
    override val chance: Double = 10.0

    override fun apply() {
        player!!.health(player.game.info.startingHealth)
    }
}
