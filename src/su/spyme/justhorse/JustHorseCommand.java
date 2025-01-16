package su.spyme.justhorse;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JustHorseCommand implements CommandExecutor, TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of("");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length > 0) {
            commandSender.sendMessage(Main.usage);
            return true;
        }

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(Main.onlyPlayer);
            return true;
        }

        if (!player.hasPermission(Main.permission)) {
            player.sendMessage(Main.noPermission);
            return true;
        }

        if (Main.cooldown.get(player) != null && Main.enableCooldown) {
            commandSender.sendMessage(Main.onCooldown);
            return true;
        }

        Main.spawnHorse(player);
        return true;
    }
}
