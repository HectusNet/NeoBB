package net.hectus.neobb;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.hectus.neobb.game.DefaultGame;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Cord;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class Commands {
    public static LiteralCommandNode<CommandSourceStack> startCommand() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("start")
                .requires(source -> source.getSender().isOp())
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("players", StringArgumentType.greedyString())
                        .executes(context -> {
                            CommandSender source = context.getSource().getSender();

                            List<@NotNull Player> players = new ArrayList<>();
                            if (source instanceof Player player) players.add(player);

                            for (String arg : context.getArgument("players", String.class).split(" ")) {
                                Player player = Bukkit.getPlayer(arg);
                                if (player != null) {
                                    if (!players.contains(player))
                                        players.add(player);
                                } else {
                                    source.sendMessage(Component.text("Could not find player: " + arg, Colors.NEGATIVE));
                                    return 1;
                                }
                            }

                            if (players.size() < 2) {
                                source.sendMessage(Component.text("You need at least 2 players to start a game.", Colors.NEGATIVE));
                                return 1;
                            }

                            Player examplePlayer = players.getFirst();
                            source.sendMessage(Component.text("Starting match with " + players.size() + " players.", Colors.POSITIVE));
                            try {
                                new DefaultGame(examplePlayer.getWorld(), players, Cord.ofLocation(examplePlayer.getLocation()), Cord.ofLocation(examplePlayer.getLocation()).add(new Cord(9, 0, 9)));
                            } catch (ReflectiveOperationException e) {
                                source.sendMessage(Component.text("Couldn't start match, due to a critical error.", Colors.NEGATIVE));
                                NeoBB.LOG.error("Couldn't start match!", e);
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
                        player.game.giveUp(player);
                    } else {
                        source.sendMessage(Component.text("You are not in any game.", Colors.NEGATIVE));
                    }
                    return 1;
                })
                .build();
    }
}
