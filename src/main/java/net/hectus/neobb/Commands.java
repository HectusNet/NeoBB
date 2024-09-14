package net.hectus.neobb;

import com.marcpg.libpg.lang.Translation;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.hectus.neobb.game.DefaultGame;
import net.hectus.neobb.game.HereGame;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.util.Colors;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("UnstableApiUsage")
public final class Commands {
    private static final List<String> MODES = List.of("default", "herestudio");

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
                        player.game.giveUp(player);
                    } else {
                        source.sendMessage(Translation.component(source.locale(), "command.not_in_game").color(Colors.NEGATIVE));
                    }
                    return 1;
                })
                .build();
    }
}
