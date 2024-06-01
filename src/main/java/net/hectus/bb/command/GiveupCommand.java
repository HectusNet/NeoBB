package net.hectus.bb.command;

import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.game.GameManager;
import net.hectus.bb.player.PlayerData;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public final class GiveupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Translation.component(Locale.getDefault(), "command.only_players").color(NamedTextColor.RED));
            return true;
        }

        PlayerData playerData = GameManager.getPlayerData(player);
        if (playerData == null) {
            player.sendMessage(Translation.component(player.locale(), "command.giveup.not_in_match").color(NamedTextColor.RED));
        } else {
            playerData.game().giveUp(playerData);
            player.sendMessage(Translation.component(player.locale(), "command.giveup.confirm").color(NamedTextColor.YELLOW));
        }
        return true;
    }
}
