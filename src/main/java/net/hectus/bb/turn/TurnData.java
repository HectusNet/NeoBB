package net.hectus.bb.turn;

import net.hectus.bb.player.PlayerData;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;

public record TurnData(PlayerData player, Turn turn, @Nullable Block block) {}
