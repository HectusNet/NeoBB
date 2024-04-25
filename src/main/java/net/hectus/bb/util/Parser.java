package net.hectus.bb.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Parser {
    public static @NotNull Location argumentToLocation(@NotNull String x, @NotNull String y, @NotNull String z, CommandSender sender) {
        if (sender instanceof Player player) {
            return new Location(player.getWorld(),
                    x.equals("~") ? player.getX() : Double.parseDouble(x),
                    y.equals("~") ? player.getY() : Double.parseDouble(y),
                    z.equals("~") ? player.getZ() : Double.parseDouble(z)
            );
        } else {
            return new Location(Bukkit.getWorld("world"), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
        }
    }

    public static @NotNull Location listToLocation(@NotNull List<Integer> list) {
        return new Location(Bukkit.getWorld("world"), list.get(0), list.get(1), list.get(2));
    }
}
