package su.spyme.justhorse;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("NullableProblems")
public class JustHorseCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of("");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
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
