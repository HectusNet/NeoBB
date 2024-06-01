package net.hectus.bb.turn;

import net.hectus.bb.player.PlayerData;
import net.hectus.bb.shop.ShopItemUtilities;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class Suggestions {
    public static List<Material> suggest(@NotNull PlayerData player, TurnData lastTurn) {
        Predicate<ItemStack> filter = i -> checkItem(i, t -> lastTurn.player().game().warp().allowed.contains(t.clazz));

        if (player.getAttack().left())
            filter = filter.and(i -> checkItem(i, t -> t.function.name().contains("COUNTER") && t.countering != null && t.countering.stream().anyMatch(f -> f.doCounter(lastTurn))));
        if (player.opponent().getDefense().left())
            filter = filter.and(i -> checkItem(i, t -> !t.function.name().contains("ATTACK")));

        return player.inv().getContents().stream().filter(filter).map(ItemStack::getType).toList();
    }

    private static boolean checkItem(ItemStack item, Predicate<Turn> predicate) {
        Turn turn = ShopItemUtilities.getTurn(item);
        if (turn == null) return false;
        return predicate.test(turn);
    }
}
