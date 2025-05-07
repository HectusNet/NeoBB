package net.hectus.neobb.turn.person_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.util.Modifiers

class PTVillagerWarp(data: PlacedStructure?, player: NeoPlayer?) : PWarpTurn(data, "villager", player) {
    override val chance: Double = 40.0

    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.Person.VILLAGER_ENTITIES)
        player.inventory.addRandom()
    }
}