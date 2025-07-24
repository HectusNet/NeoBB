package net.hectus.neobb.player

import com.marcpg.libpg.item.ItemBuilder
import com.marcpg.libpg.util.asString
import com.marcpg.libpg.util.component
import net.hectus.neobb.modes.shop.util.Items
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.person_game.Category
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.stream.IntStream

class NeoInventory(private var player: NeoPlayer) {
    var coins: Int = player.game.info.coins
    var shopDone: Boolean = false

    val deck: Array<ItemStack?> = arrayOfNulls(player.game.info.deckSize)
    val turns: Array<Turn<*>?> = arrayOfNulls(player.game.info.deckSize)

    init {
        sync()
    }

    fun switchOwner(newOwner: NeoPlayer) {
        player = newOwner
    }

    fun clear() {
        for (i in deck.indices) {
            deck[i] = null
            turns[i] = null
        }
        sync()
    }

    fun isEmpty(): Boolean = deck.all { it == null }
    fun isFull(): Boolean = deck.all { it != null }
    fun slotsLeft(): Int = deck.count { it == null }

    fun setSlot(slot: Int, item: ItemStack?, turn: Turn<*>?) {
        deck[slot] = item
        turns[slot] = turn
        sync()
    }

    fun removeOne(item: ItemStack) {
        val index = deck.indexOfLast { item.type == it?.type && item.displayName().asString() == it.displayName().asString() }
        if (index == -1) return
        deck[index] = null
        turns[index] = null
    }

    fun sell(turn: Turn<*>) {
        turn.items.forEach { removeOne(it) }
        addCoins(turn.cost ?: 0)
    }

    fun clearSlot(slot: Int) {
        setSlot(slot, null, null)
    }

    fun clearFirst(predicate: (ItemStack, Turn<*>) -> Boolean) {
        for (i in deck.indices) {
            if (deck[i] != null && predicate(deck[i]!!, turns[i]!!)) {
                clearSlot(i)
                return
            }
        }
    }

    fun add(turn: Turn<*>) {
        turn.items.forEach { i -> add(player.shop.makeItem(turn, i, false), turn) }
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
            add(player.game.info.turns.random())
        }
    }

    fun removeRandom() {
        if (isEmpty()) return

        setSlot(IntStream.range(0, deck.size)
            .filter { i -> deck[i] != null }
            .boxed()
            .toList().random(), null, null)
    }

    fun fillInRandomly() {
        for (item in deck) {
            if (item == null) addRandom()
        }
    }

    fun allows(turn: Turn<*>): Boolean {
        var count = 0
        for (item in deck) {
            if (item != null && item.type == turn.mainItem.type)
                count++
        }
        return count < (if (turn is Category) turn.categoryMaxPerDeck else turn.maxAmount)
    }

    @Synchronized
    fun addCoins(coins: Int) {
        this.coins += coins
        sync()
    }

    @Synchronized
    fun removeCoins(coins: Int): Boolean {
        if (coins > this.coins) return false
        this.coins -= coins
        sync()
        return true
    }

    fun sync() {
        val inv = player.player.inventory
        inv.clear()

        for (i in deck.indices) {
            val item = if (deck[i] == null) Items.BLACK_BACKGROUND else deck[i]!!.clone()
            val turn = turns[i]

            if (player.game.difficulty.suggestions && turn?.goodChoice(player) ?: false)
                item.editMeta { it.setEnchantmentGlintOverride(true) }

            inv.setItem(i, item)
        }

        inv.setItem(13, ItemBuilder((if (coins == 0) Material.RESIN_BRICK else Material.GOLD_INGOT)).apply {
            amount(coins.coerceAtLeast(1))
            name(player.locale().component("item-lore.cost.value", coins.toString()))
        }.build())
    }
}
