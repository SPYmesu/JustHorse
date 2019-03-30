package su.spyme.moonhorse;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHorse implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args){
        if(!(commandSender instanceof Player)) return false;

        if(!commandSender.hasPermission(Main.permission)){
            commandSender.sendMessage(Main.noPermission);
            return false;
        }

        if(args.length != 0){
            commandSender.sendMessage(Main.usage);
            return false;
        }

        if(Main.cooldown.get(commandSender) != null && Main.enableCooldown){
            commandSender.sendMessage(Main.onCooldown);
            return false;
        }

        Main.spawnHorse((Player)commandSender);
        return true;
    }
}
