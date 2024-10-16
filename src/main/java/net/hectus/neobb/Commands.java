package net.hectus.neobb;

import com.marcpg.libpg.lang.Translation;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.hectus.neobb.game.mode.DefaultGame;
import net.hectus.neobb.game.mode.HereGame;
import net.hectus.neobb.game.mode.LegacyGame;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Cord;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("UnstableApiUsage")
public final class Commands {
    private static final List<String> MODES = List.of("default", "herestudio", "legacy");

    public static LiteralCommandNode<CommandSourceStack> startCommand() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("start")
                .requires(source -> source.getSender().hasPermission("neobb.start"))
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("mode", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            MODES.forEach(builder::suggest);
                            return builder.buildFuture();
                        })
                        .then(RequiredArgumentBuilder.<CommandSourceStack, PlayerSelectorArgumentResolver>argument("players", ArgumentTypes.players())
                                .executes(context -> {
                                    CommandSender source = context.getSource().getSender();
                                    Locale l = source instanceof Player p ? p.locale() : Locale.getDefault();

                                    List<Player> players = context.getArgument("players", PlayerSelectorArgumentResolver.class).resolve(context.getSource());
                                    if (players.size() < 2) {
                                        source.sendMessage(Translation.component(l, "command.start.not_enough_players").color(Colors.NEGATIVE));
                                        return 1;
                                    }

                                    source.sendMessage(Translation.component(l, "command.start.starting", players.size()).color(Colors.POSITIVE));
                                    try {
                                        switch (context.getArgument("mode", String.class)) {
                                            case "default" -> new DefaultGame(true, players.getFirst().getWorld(), players);
                                            case "herestudio" -> new HereGame(false, players.getFirst().getWorld(), players);
                                            case "legacy" -> new LegacyGame(false, players.getFirst().getWorld(), players);
                                            default -> source.sendMessage(Translation.component(l, "command.start.unknown_mode").color(Colors.NEGATIVE));
                                        }
                                    } catch (Exception e) {
                                        source.sendMessage(Translation.component(l, "command.start.error").color(Colors.NEGATIVE));
                                        NeoBB.LOG.error("Could not start match!", e);
                                    }

                                    return 1;
                                })
                        )
                )
                .build();
    }

    public static LiteralCommandNode<CommandSourceStack> giveupCommand() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("giveup")
                .requires(source -> source.getSender() instanceof Player)
                .executes(context -> {
                    Player source = (Player) context.getSource().getSender();
                    NeoPlayer player = GameManager.player(source, true);
                    if (player != null) {
                        source.sendMessage(Translation.component(source.locale(), "command.giveup.confirm").color(Colors.YELLOW));
                        player.game.giveUp(player);
                    } else {
                        source.sendMessage(Translation.component(source.locale(), "command.not_in_game").color(Colors.NEGATIVE));
                    }
                    return 1;
                })
                .build();
    }

    public static LiteralCommandNode<CommandSourceStack> structureCommand() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("structure")
                .requires(source -> source.getSender().hasPermission("neobb.structures"))
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("save")
                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("name", StringArgumentType.word())
                                .then(RequiredArgumentBuilder.<CommandSourceStack, World>argument("world", ArgumentTypes.world())
                                        .then(RequiredArgumentBuilder.<CommandSourceStack, BlockPositionResolver>argument("corner1", ArgumentTypes.blockPosition())
                                                .then(RequiredArgumentBuilder.<CommandSourceStack, BlockPositionResolver>argument("corner2", ArgumentTypes.blockPosition())
                                                        .executes(context -> {
                                                            String name = context.getArgument("name", String.class);

                                                            if (StructureManager.structure(name) != null) {
                                                                context.getSource().getSender().sendMessage(Component.text("Structure with name \"" + name + "\" already exists!", Colors.NEGATIVE));
                                                                context.getSource().getSender().sendMessage(Component.text("You can remove it using \"/structure remove " + name + "\"!", Colors.EXTRA));
                                                                return 1;
                                                            }

                                                            World world = context.getArgument("world", World.class);
                                                            Location corner1 = context.getArgument("corner1", BlockPositionResolver.class).resolve(context.getSource()).toLocation(world);
                                                            Location corner2 = context.getArgument("corner2", BlockPositionResolver.class).resolve(context.getSource()).toLocation(world);

                                                            Structure structure = new Structure(name, world, Cord.ofLocation(corner1), Cord.ofLocation(corner2));
                                                            structure.save();
                                                            StructureManager.add(structure);
                                                            context.getSource().getSender().sendMessage(Component.text("Successfully saved structure with name: " + structure.name, Colors.POSITIVE));

                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                )
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("remove")
                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("name", StringArgumentType.word())
                                .executes(context -> {
                                    String name = context.getArgument("name", String.class);
                                    Structure structure = StructureManager.structure(name);
                                    if (structure != null) {
                                        StructureManager.remove(structure);
                                        context.getSource().getSender().sendMessage(Component.text("Successfully removed structure with name: " + name, Colors.NEUTRAL));
                                    } else {
                                        context.getSource().getSender().sendMessage(Component.text("Could not find structure with name: " + name, Colors.NEGATIVE));
                                    }

                                    return 1;
                                })
                        )
                )
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("list")
                        .executes(context -> {
                            CommandSender source = context.getSource().getSender();
                            for (Structure structure : StructureManager.getStructures()) {
                                source.sendMessage(Component.text("- " + structure.name + "(" + structure.materials.size() + ")"));
                            }
                            return 1;
                        })
                )
                .build();
    }
}
