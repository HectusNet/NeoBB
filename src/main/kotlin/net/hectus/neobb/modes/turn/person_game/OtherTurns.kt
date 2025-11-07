package net.hectus.neobb.modes.turn.person_game

import com.marcpg.libpg.display.location
import com.marcpg.libpg.util.MinecraftTime
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.OtherTurn
import net.hectus.neobb.util.Modifiers
import org.bukkit.Art
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Painting
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.window.Window

object PTArmorStand : OtherTurn<ArmorStand>("armor_stand"), DefensiveCounterCategory {
    override val mode: String = "person"

    override val event: TurnEvent = TurnEvent.CUSTOM
}

object PTPainting : OtherTurn<Painting>("painting"), BuffCategory {
    override val mode: String = "person"

    override val event: TurnEvent = TurnEvent.CUSTOM

    override fun apply(exec: TurnExec<Painting>) {
        when (exec.data.art) {
            Art.ALBAN -> Luck(30).invoke(exec.player)
            Art.AZTEC, Art.AZTEC2 -> {
                exec.player.inventory.removeRandom()
                exec.player.inventory.removeRandom()
            }
            Art.BOMB -> exec.game.players.forEach { it.damage(5.0) }
            Art.KEBAB -> exec.player.inventory.removeRandom()
            Art.PLANT -> ExtraTurn(if (Randomizer.boolByChance(25.0)) 2 else 1).invoke(exec.player)
            Art.WASTELAND -> exec.game.players.forEach { p ->
                p.inventory.clear()
                p.inventory.fillInRandomly()
            }
            Art.COURBET -> exec.game.time = MinecraftTime.MIDNIGHT
            Art.POOL -> exec.player.luck += 99
            Art.SEA, Art.BUST -> exec.game.eliminate(exec.player)
            Art.CREEBET -> {
                val gui = Gui.normal().setStructure("0 1 2 3 4 5 6 7 8 9")
                val deck = exec.player.targetPlayer().inventory.deck
                for (i in deck.indices)
                    gui.addIngredient(('0'.code + i).toChar(), deck[i] ?: ItemStack.empty())
                Window.single().setTitle("Opponent Deck").setGui(gui.build()).open(exec.player.player)
            }
            Art.SUNSET -> {
                val players = exec.game.players
                val temp = players.first().health
                for (i in 0 until players.size - 1) {
                    players[i].health = players[i + 1].health
                }
                players.last().health = temp
            }
            Art.GRAHAM -> exec.player.addModifier(Modifiers.Player.NO_MOVE)
            Art.WANDERER -> exec.game.addModifier(Modifiers.Game.Person.NO_WIN_CONS)
            Art.MATCH -> exec.player.luck += 20
            Art.SKULL_AND_ROSES -> exec.game.arena.clear()
            Art.STAGE -> exec.player.opponents().forEach { p ->
                p.location().block.type = Material.COBWEB
                if (Randomizer.boolByChance(20.0))
                    p.game.eliminate(p)
            }
            Art.VOID -> {
                ExtraTurn().invoke(exec.player)
                exec.player.addArmor(1.0)
            }
            Art.WITHER -> exec.game.eliminate(if (Randomizer.boolByChance(20.0)) exec.player.targetPlayer() else exec.player)
        }
    }
}
