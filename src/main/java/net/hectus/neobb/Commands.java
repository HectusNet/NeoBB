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
import net.hectus.neobb.cosmetic.PlaceParticle;
import net.hectus.neobb.cosmetic.PlayerAnimation;
import net.hectus.neobb.game.DummyGame;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.game.mode.CardGame;
import net.hectus.neobb.game.mode.DefaultGame;
import net.hectus.neobb.game.mode.LegacyGame;
import net.hectus.neobb.game.mode.PersonGame;
import net.hectus.neobb.game.util.Difficulty;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Cord;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public final class Commands {
    private static final List<String> MODES = List.of("default", "card", "legacy", "person98");

    public static LiteralCommandNode<CommandSourceStack> startCommand() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("games")
                .requires(source -> source.getSender().hasPermission("neobb.games"))
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("start")
                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("mode", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    MODES.forEach(builder::suggest);
                                    return builder.buildFuture();
                                })
                                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("difficulty", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            for (Difficulty v : Difficulty.values()) {
                                                builder.suggest(v.name());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .then(RequiredArgumentBuilder.<CommandSourceStack, PlayerSelectorArgumentResolver>argument("players", ArgumentTypes.players())
                                                .executes(context -> {
                                                    CommandSender source = context.getSource().getSender();
                                                    Locale l = source instanceof Player p ? p.locale() : Locale.getDefault();

                                                    List<Player> players = context.getArgument("players", PlayerSelectorArgumentResolver.class).resolve(context.getSource());
                                                    if (players.size() < 2) {
                                                        source.sendMessage(Translation.component(l, "command.games.start.not_enough_players").color(Colors.NEGATIVE));
                                                        return 1;
                                                    }
                                                    Difficulty difficulty = Difficulty.valueOf(context.getArgument("difficulty", String.class));

                                                    source.sendMessage(Translation.component(l, "command.games.start.starting", players.size()).color(Colors.POSITIVE));
                                                    try {
                                                        switch (context.getArgument("mode", String.class)) {
                                                            case "default" -> new DefaultGame(difficulty, players.getFirst().getWorld(), players);
                                                            case "card" -> new CardGame(difficulty, players.getFirst().getWorld(), players);
                                                            case "legacy" -> new LegacyGame(difficulty, players.getFirst().getWorld(), players);
                                                            case "person98" -> new PersonGame(difficulty, players.getFirst().getWorld(), players);
                                                            default -> source.sendMessage(Translation.component(l, "command.games.start.unknown_mode").color(Colors.NEGATIVE));
                                                        }
                                                    } catch (Exception e) {
                                                        source.sendMessage(Translation.component(l, "command.games.start.error").color(Colors.NEGATIVE));
                                                        NeoBB.LOG.error("Could not start match!", e);
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("stop")
                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("id", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    GameManager.GAMES.forEach(g -> builder.suggest(g.id));
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    CommandSender source = context.getSource().getSender();
                                    Locale l = source instanceof Player p ? p.locale() : Locale.getDefault();

                                    Game game = GameManager.game(context.getArgument("id", String.class));
                                    if (game == null) {
                                        source.sendMessage(Translation.component(l, "command.games.not_found", context.getArgument("id", String.class)).color(Colors.NEGATIVE));
                                        return 1;
                                    }

                                    game.end(true);
                                    source.sendMessage(Translation.component(l, "command.games.stop.success", game.id).color(Colors.NEUTRAL));

                                    return 1;
                                })
                ))
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("list")
                        .executes(context -> {
                            CommandSender source = context.getSource().getSender();
                            for (Game game : GameManager.GAMES) {
                                source.sendMessage(Component.text("==== ", Colors.EXTRA).append(Component.text(game.id, Colors.ACCENT)).append(Component.text(" ====", Colors.EXTRA)));
                                source.sendMessage(Component.text("> Players: ", Colors.EXTRA).append(Component.text(game.players().size() + "/" + game.initialPlayers().size(), Colors.SECONDARY)));
                                if (game.timeLeft() != null) source.sendMessage(Component.text("> Time: ", Colors.EXTRA).append(Component.text(game.timeLeft().getPreciselyFormatted(), Colors.SECONDARY)));
                                source.sendMessage(Component.text("> Difficulty: ", Colors.EXTRA).append(Component.text(game.difficulty.name(), Colors.SECONDARY)));
                                if (game.history() != null) source.sendMessage(Component.text("> Played Turns: ", Colors.EXTRA).append(Component.text(game.history().size(), Colors.SECONDARY)));
                                if (game.currentPlayer() != null) source.sendMessage(Component.text("> Turning: ", Colors.EXTRA).append(Component.text(game.currentPlayer().player.getName(), Colors.SECONDARY)));
                            }
                            return 1;
                        })
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
                                .suggests((context, builder) -> {
                                    StructureManager.getStructures().forEach(s -> builder.suggest(s.name));
                                    return builder.buildFuture();
                                })
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
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("place")
                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("name", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    StructureManager.getStructures().forEach(s -> builder.suggest(s.name));
                                    return builder.buildFuture();
                                })
                                .then(RequiredArgumentBuilder.<CommandSourceStack, BlockPositionResolver>argument("location", ArgumentTypes.blockPosition())
                                        .executes(context -> {
                                            String name = context.getArgument("name", String.class);
                                            Location location = context.getArgument("location", BlockPositionResolver.class).resolve(context.getSource()).toLocation(context.getSource().getLocation().getWorld());

                                            Structure structure = StructureManager.structure(name);
                                            if (structure != null) {
                                                structure.place(location);
                                            } else {
                                                context.getSource().getSender().sendMessage(Component.text("Could not find structure with name: " + name, Colors.NEGATIVE));
                                            }
                                            return 1;
                                        })
                                )
                        )
                )
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("check")
                        .requires(source -> source.getSender() instanceof Player)
                        .executes(context -> {
                            Player player = (Player) context.getSource().getSender();
                            Game game = new DummyGame(player);
                            game.arena.scanBlocks();

                            Structure structure = StructureManager.match(game.arena);
                            if (structure == null) {
                                player.sendMessage(Component.text("No structure found!", Colors.NEGATIVE));
                            } else {
                                player.sendMessage(Component.text("Found structure: " + structure.name, Colors.POSITIVE));
                            }
                            return 1;
                        })
                )
                .build();
    }

    public static LiteralCommandNode<CommandSourceStack> debugCommand() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("debug")
                .requires(source -> source.getSender().hasPermission("neobb.debug"))
                .then(LiteralArgumentBuilder.<CommandSourceStack>literal("test-effect")
                        .then(LiteralArgumentBuilder.<CommandSourceStack>literal("player-animation")
                                .then(RequiredArgumentBuilder.<CommandSourceStack, PlayerSelectorArgumentResolver>argument("player", ArgumentTypes.player())
                                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("animation", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    for (PlayerAnimation v : PlayerAnimation.values())
                                                        builder.suggest(v.name().toLowerCase());
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> {
                                                    Player player = context.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(context.getSource()).getFirst();
                                                    PlayerAnimation.valueOf(context.getArgument("animation", String.class).toUpperCase()).action.accept(player, Cord.ofLocation(player.getLocation()).add(new Cord(-4, 0, -4)));
                                                    return 1;
                                                })
                                        )
                                )
                        )
                        .then(LiteralArgumentBuilder.<CommandSourceStack>literal("place-particle")
                                .then(RequiredArgumentBuilder.<CommandSourceStack, BlockPositionResolver>argument("block", ArgumentTypes.blockPosition())
                                        .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("particle", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    for (PlaceParticle v : PlaceParticle.values())
                                                        builder.suggest(v.name().toLowerCase());
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> {
                                                    World world = context.getSource().getSender() instanceof Player player ? player.getWorld() : Objects.requireNonNull(Bukkit.getWorld("world"));
                                                    Location block = context.getArgument("block", BlockPositionResolver.class).resolve(context.getSource()).toLocation(world);
                                                    PlaceParticle.valueOf(context.getArgument("particle", String.class).toUpperCase()).spawn(block);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
                .build();
    }
}
