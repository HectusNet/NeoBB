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
                    x.startsWith("~") ? player.getX() + Double.parseDouble(x.replace("~", "")) : Double.parseDouble(x),
                    y.startsWith("~") ? player.getY() + Double.parseDouble(y.replace("~", "")) : Double.parseDouble(y),
                    z.startsWith("~") ? player.getZ() + Double.parseDouble(z.replace("~", "")) : Double.parseDouble(z)
            );
        } else {
            return new Location(Bukkit.getWorld("world"), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
        }
    }

    public static @NotNull Location listToLocation(@NotNull List<Integer> list) {
        return new Location(Bukkit.getWorld("world"), list.get(0), list.get(1), list.get(2));
    }
}
