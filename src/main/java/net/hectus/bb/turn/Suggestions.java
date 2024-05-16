package net.hectus.bb.turn;

import net.hectus.bb.player.PlayerData;
import net.hectus.bb.shop.ShopItemUtilities;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Suggestions {
    public static List<Material> suggest(@NotNull PlayerData player, TurnData lastTurn) {
        Predicate<Material> filter = m -> checkMaterial(m, t -> lastTurn.player().game().warp().allowed.contains(t.clazz));

        if (player.getAttack().left())
            filter = filter.and(m -> checkMaterial(m, t -> t.function.name().contains("COUNTER") && t.countering != null && t.countering.stream().anyMatch(f -> f.doCounter(lastTurn))));
        if (player.opponent().getDefense().left())
            filter = filter.and(m -> checkMaterial(m, t -> !t.function.name().contains("ATTACK")));

        return player.inv().getContents().stream().map(ItemStack::getType).filter(filter).toList();
    }

    public static List<Turn> suggestAll(@NotNull PlayerData player, TurnData lastTurn) {
        Predicate<Turn> filter = t -> lastTurn.player().game().warp().allowed.contains(t.clazz);

        if (player.getAttack().left())
            filter = filter.and(t -> t.function.name().contains("COUNTER") && t.countering != null && t.countering.stream().anyMatch(f -> f.doCounter(lastTurn)));
        if (player.opponent().getDefense().left())
            filter = filter.and(t -> !t.function.name().contains("ATTACK"));

        return Arrays.stream(Turn.values()).filter(filter).toList();
    }

    private static boolean checkMaterial(Material material, Predicate<Turn> predicate) {
        Turn turn = ShopItemUtilities.getTurn(material);
        if (turn == null) return false;
        return predicate.test(turn);
    }
}
