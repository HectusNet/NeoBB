package net.hectus.bb.command;

import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.game.Game;
import net.hectus.bb.game.GameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ChallengeCommand implements TabExecutor {
    public static final List<Challenge> challenges = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Translation.component(Locale.getDefault(), "command.only_players").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("accept")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Translation.component(player.locale(), "command.player_not_found", args[1]).color(NamedTextColor.RED));
                return true;
            }
            if (challenges.contains(new Challenge(target, player))) {
                challenges.remove(new Challenge(target, player));
                challenges.remove(new Challenge(player, target));
                player.sendMessage(Translation.component(player.locale(), "command.challenge.start", target.getName()));
                target.sendMessage(Translation.component(target.locale(), "command.challenge.start", player.getName()));
                GameManager.GAMES.add(new Game(player.getWorld(), target, player));
            } else {
                player.sendMessage(Translation.component(player.locale(), "command.challenge.no_incoming", args[1]).color(NamedTextColor.RED));
            }
        } else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Translation.component(player.locale(), "command.player_not_found", args[0]).color(NamedTextColor.RED));
                return true;
            }
            if (challenges.contains(new Challenge(player, target))) {
                player.sendMessage(Translation.component(player.locale(), "command.challenge.already", args[0]).color(NamedTextColor.RED));
            } else {
                challenges.add(new Challenge(player, target));
                player.sendMessage(Translation.component(player.locale(), "command.challenge.confirm", args[0]));
                target.sendMessage(Translation.component(target.locale(), "command.challenge.confirm-target.1", player.getName()).color(NamedTextColor.YELLOW)
                        .appendSpace()
                        .append(Translation.component(target.locale(), "command.challenge.confirm-target.2").color(NamedTextColor.GOLD)
                                .clickEvent(ClickEvent.runCommand("/challenge accept " + player.getName()))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to accept!", NamedTextColor.GREEN))))
                        .appendSpace()
                        .append(Translation.component(target.locale(), "command.challenge.confirm-target.3").color(NamedTextColor.YELLOW)));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(s -> s.toLowerCase().contains(args[0].toLowerCase()) && !s.equals(sender.getName()))

                    .toList();
        }
        return List.of();
    }

    public record Challenge(Player challenger, Player challenged) {
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Challenge challenge) {
                return challenge.challenger.getUniqueId().equals(challenger.getUniqueId()) &&
                        challenge.challenged.getUniqueId().equals(challenged.getUniqueId());
            } else {
                return false;
            }
        }
    }
}
