package net.hectus.neobb.modes.turn.person_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers

class PTVillagerWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "villager", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.VILLAGER

    override val chance: Double = 40.0

    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.Person.VILLAGER_ENTITIES)
        player.inventory.addRandom()
    }
}
