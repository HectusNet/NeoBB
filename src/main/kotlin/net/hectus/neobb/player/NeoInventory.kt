package net.hectus.neobb.player

import com.marcpg.libpg.util.ItemBuilder
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.shop.util.Items
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.person_game.categorization.Category
import net.hectus.neobb.util.component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.stream.IntStream

class NeoInventory(private var player: NeoPlayer) {
    var coins: Int = player.game.info.coins
    var shopDone: Boolean = false

    val deck: Array<ItemStack?> = arrayOfNulls(player.game.info.deckSize)
    val dummyTurns: Array<Turn<*>?> = arrayOfNulls(player.game.info.deckSize)

    init {
        sync()
    }

    fun switchOwner(newOwner: NeoPlayer) {
        player = newOwner
    }

    fun clear() {
        for (i in deck.indices) {
            deck[i] = null
            dummyTurns[i] = null
        }
        sync()
    }

    fun isEmpty(): Boolean = deck.all { it == null }
    fun isFull(): Boolean = deck.all { it != null }

    fun setSlot(slot: Int, item: ItemStack?, turn: Turn<*>?) {
        deck[slot] = item
        dummyTurns[slot] = turn
        sync()
    }

    fun sellOne(item: ItemStack) {
        val index = deck.indexOfLast { item.isSimilar(it) }
        if (index == -1) return

        addCoins(dummyTurns[index]?.cost ?: 0)
        clearSlot(index)
    }

    fun clearSlot(slot: Int) {
        setSlot(slot, null, null)
    }

    fun add(turn: Turn<*>) {
        turn.items().forEach { i -> add(ItemBuilder(i)
            .lore(player.shop.loreBuilder.turn(turn).buildWithTooltips(player.locale()))
            .build(), turn)
        }
    }

    fun add(item: ItemStack, turn: Turn<*>) {
        for (i in deck.indices) {
            if (deck[i] == null) {
                setSlot(i, item, turn)
                return
            }
        }
        throw ArrayIndexOutOfBoundsException("Deck is already full!")
    }

    fun addRandom() {
        if (isFull()) return

        runCatching {
            add(Randomizer.fromCollection(player.shop.dummyTurns))
        }
    }

    fun removeRandom() {
        if (isEmpty()) return

        setSlot(Randomizer.fromCollection(IntStream.range(0, deck.size)
            .filter { i -> deck[i] != null }
            .boxed()
            .toList()), null, null)
    }

    fun fillInRandomly() {
        for (item in deck) {
            if (item == null) addRandom()
        }
    }

    fun allows(turn: Turn<*>, material: Material): Boolean {
        var count = 0
        for (item in deck) {
            if (item != null && item.type == material)
                count++
        }
        return count < (if (turn is Category) turn.categoryMaxPerDeck else turn.maxAmount)
    }

    @Synchronized
    fun removeCoins(coins: Int): Boolean {
        if (coins > this.coins) return false
        this.coins -= coins
        sync()
        return true
    }

    @Synchronized
    fun addCoins(coins: Int) {
        this.coins += coins
        sync()
    }

    fun sync() {
        val inv = player.player.inventory
        inv.clear()

        for (i in deck.indices) {
            val item = if (deck[i] == null) Items.BLACK_BACKGROUND else deck[i]!!.clone()
            val dummyTurn = if (dummyTurns[i] == null) Turn.DUMMY else dummyTurns[i]!!

            if (player.game.difficulty.suggestions && dummyTurn.goodChoice(player))
                item.editMeta { it.setEnchantmentGlintOverride(true) }

            inv.setItem(i, item)
        }

        inv.setItem(13, ItemBuilder((if (coins == 0) Material.RESIN_BRICK else Material.GOLD_INGOT))
            .amount(coins.coerceIn(1, 64))
            .name(player.locale().component("item-lore.cost.value", coins.toString()))
            .build())
    }
}
