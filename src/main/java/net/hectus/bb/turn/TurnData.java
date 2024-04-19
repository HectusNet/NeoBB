package net.hectus.bb.turn;

import net.hectus.bb.player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

public record TurnData(PlayerData player, Turn turn, @Nullable Block block, @Nullable Entity entity) {
    public static final TurnData EMPTY = new TurnData(null, null, null, null);
}
