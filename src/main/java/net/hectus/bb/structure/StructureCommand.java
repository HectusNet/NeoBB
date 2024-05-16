package net.hectus.bb.structure;

import com.marcpg.libpg.storing.Pair;
import com.marcpg.libpg.text.Completer;
import net.hectus.bb.util.Parser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class StructureCommand implements TabExecutor {
    private static final List<String> OPERATIONS = List.of("reload", "info", "remove", "save", "place");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1 && args[0].equals("reload")) {
            StructureManager.load();
            sender.sendMessage(Component.text("Successfully reloaded all structures!", NamedTextColor.GREEN));
        } else if (args.length == 2) {
            Structure structure = StructureManager.get(args[1]);
            if (structure == null) {
                sender.sendMessage(Component.text("Couldn't find any Structure named \"" + args[1] + "\"!", NamedTextColor.RED));
                return true;
            }

            if (args[0].equals("info")) {
                sender.sendMessage(Component.text("Info about Structure \"" + structure.name() + "\":", NamedTextColor.YELLOW));
                sender.sendMessage(Component.text("Blocks: ").append(Component.text(structure.data().size(), NamedTextColor.GRAY)));
                Pair<Material, Integer> mostCommonMaterial = structure.mostCommonMaterial();
                sender.sendMessage(Component.text("Most Common: ").append(Component.text(mostCommonMaterial.left().name() + " - x" + mostCommonMaterial.right(), NamedTextColor.GRAY)));
            } else if (args[0].equals("remove")) {
                if (StructureManager.remove(args[1])) {
                    sender.sendMessage(Component.text("Successfully removed the structure \"" + args[1] + "\"! You might need to reload for this to take effect!", NamedTextColor.YELLOW));
                } else {
                    sender.sendMessage(Component.text("Couldn't find any Structure named \"" + args[1] + "\"!", NamedTextColor.RED));
                }
            }
        } else if (args.length == 5 && args[0].equals("place")) {
            Structure structure = StructureManager.get(args[1]);
            if (structure != null) {
                long start = System.currentTimeMillis();
                structure.place(Parser.argumentToLocation(args[2], args[3], args[4], sender));
                sender.sendMessage(Component.text("Done! Took " + (System.currentTimeMillis() - start) + "ms!"));
            } else {
                sender.sendMessage(Component.text("Couldn't find any Structure named \"" + args[1] + "\"!", NamedTextColor.RED));
            }
        } else if (args.length == 8 && args[0].equals("save")) {
            if (args[1].matches("^[a-z0-9_-]+$")) {
                long start = System.currentTimeMillis();
                Location corner1 = Parser.argumentToLocation(args[2], args[3], args[4], sender);
                Location corner2 = Parser.argumentToLocation(args[5], args[6], args[7], sender);
                StructureManager.add(Structure.save(sender instanceof Player p ? p.getWorld() : corner1.getWorld(), corner1, corner2, args[1]));
                StructureManager.save();
                sender.sendMessage(Component.text("Done! Took " + (System.currentTimeMillis() - start) + "ms!"));
            } else {
                sender.sendMessage(Component.text("Invalid Structure name! Name should only contain a-z, 0-9, underscores and hyphens.", NamedTextColor.RED));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return Completer.startComplete(args[0], OPERATIONS);
        } else if (args.length == 2) {
            return switch (args[0]) {
                case "info", "remove", "place" -> Completer.semiSmartComplete(args[1], StructureManager.names());
                case "save" -> List.of(args[1].toLowerCase());
                default -> List.of();
            };
        } else if (args.length >= 3 && (args[0].equals("save") || args[0].equals("place")) && args.length <= (args[0].equals("save") ? 8 : 5) && sender instanceof Player player) {
            Block t = player.getTargetBlockExact(16);
            return switch (args.length) {
                case 3, 6 -> t == null ? List.of("~ ~ ~") : List.of(t.getX() + " " + t.getY() + " " + t.getZ(), "~ ~ ~");
                case 4, 7 -> t == null ? List.of(  "~ ~") : List.of(                 t.getY() + " " + t.getZ(),   "~ ~");
                default ->   t == null ? List.of(    "~") : List.of(                            " " + t.getZ(),     "~");
            };
        }
        return List.of();
    }
}
