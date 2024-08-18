package net.hectus.neobb;

import com.marcpg.libpg.lang.Translation;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.hectus.neobb.game.DefaultGame;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.util.Colors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("UnstableApiUsage")
public class Commands {
    public static LiteralCommandNode<CommandSourceStack> startCommand() {
        return LiteralArgumentBuilder.<CommandSourceStack>literal("start")
                .requires(source -> source.getSender().isOp())
                .then(RequiredArgumentBuilder.<CommandSourceStack, String>argument("players", StringArgumentType.greedyString())
                        .executes(context -> {
                            CommandSender source = context.getSource().getSender();
                            Locale l = source instanceof Player p ? p.locale() : Locale.getDefault();

                            List<@NotNull Player> players = new ArrayList<>();
                            if (source instanceof Player player) players.add(player);

                            for (String arg : context.getArgument("players", String.class).split(" ")) {
                                Player player = Bukkit.getPlayer(arg);
                                if (player != null) {
                                    if (!players.contains(player))
                                        players.add(player);
                                } else {
                                    source.sendMessage(Translation.component(l, "command.player_not_found", arg).color(Colors.NEGATIVE));
                                    return 1;
                                }
                            }

                            if (players.size() < 2) {
                                source.sendMessage(Translation.component(l, "command.start.not_enough_players").color(Colors.NEGATIVE));
                                return 1;
                            }

                            Player examplePlayer = players.getFirst();
                            source.sendMessage(Translation.component(l, "command.start.starting", players.size()).color(Colors.POSITIVE));
                            try {
                                new DefaultGame(true, examplePlayer.getWorld(), players);
                            } catch (Exception e) {
                                source.sendMessage(Translation.component(l, "command.start.error").color(Colors.NEGATIVE));
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
                        source.sendMessage(Translation.component(source.locale(), "command.not_in_game").color(Colors.NEGATIVE));
                    }
                    return 1;
                })
                .build();
    }
}
