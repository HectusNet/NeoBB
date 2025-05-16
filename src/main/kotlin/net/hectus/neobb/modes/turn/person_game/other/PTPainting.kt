package net.hectus.neobb.modes.turn.person_game.other

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.default_game.other.OtherTurn
import net.hectus.neobb.modes.turn.person_game.categorization.BuffCategory
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.Art
import org.bukkit.Material
import org.bukkit.entity.Painting
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.window.Window

class PTPainting(data: Painting?, cord: Cord?, player: NeoPlayer?) : OtherTurn<Painting>(data, cord, player), BuffCategory {
    override fun item(): ItemStack = ItemStack(Material.PAINTING)

    override fun buffs(): List<Buff<*>> = emptyList()

    override fun apply() {
        player!! // Force non-null.
        when (data!!.art) {
            Art.ALBAN -> Luck(30).apply(player)
            Art.AZTEC, Art.AZTEC2 -> {
                player.inventory.removeRandom()
                player.inventory.removeRandom()
            }
            Art.BOMB -> player.game.players.forEach { it.damage(5.0) }
            Art.KEBAB -> player.inventory.removeRandom()
            Art.PLANT -> ExtraTurn(if (Randomizer.boolByChance(25.0)) 2 else 1).apply(player)
            Art.WASTELAND -> player.game.players.forEach { p ->
                p.inventory.clear()
                p.inventory.fillInRandomly()
            }
            Art.COURBET -> player.game.time = MinecraftTime.MIDNIGHT
            Art.POOL -> player.addLuck(99)
            Art.SEA, Art.BUST -> player.game.eliminate(player)
            Art.CREEBET -> {
                val gui = Gui.normal().setStructure("0 1 2 3 4 5 6 7 8 9")
                val deck = player.nextPlayer().inventory.deck
                for (i in deck.indices)
                    gui.addIngredient(('0'.code + i).toChar(), deck[i] ?: ItemStack.empty())

                Window.single().setTitle("Opponent Deck").setGui(gui.build()).open(player.player)
            }
            Art.SUNSET -> {
                val players = player.game.players
                val temp = players.first().health
                for (i in 0..<players.size - 1) {
                    players[i].health(players[i + 1].health)
                }
                players.last().health(temp)
            }
            Art.GRAHAM -> player.addModifier(Modifiers.Player.NO_MOVE)
            Art.WANDERER -> player.game.addModifier(Modifiers.Game.Person.NO_WIN_CONS)
            Art.MATCH -> player.addLuck(20)
            Art.SKULL_AND_ROSES -> player.game.arena.clear()
            Art.STAGE -> player.opponents().forEach { p ->
                p.location().block.type = Material.COBWEB
                if (Randomizer.boolByChance(20.0))
                    p.game.eliminate(p)
            }
            Art.VOID -> {
                ExtraTurn().apply(player)
                player.addArmor(1.0)
            }
            Art.WITHER -> player.game.eliminate(if (Randomizer.boolByChance(20.0)) player.nextPlayer() else player)
        }
    }
}
