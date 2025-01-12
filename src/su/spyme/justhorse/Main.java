package su.spyme.justhorse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import su.spyme.justhorse.gui.GuiListener;
import su.spyme.justhorse.utils.GuiMenu;

import java.util.*;

public class Main extends JavaPlugin implements Listener{

    //Права
    static String permission;

    //Настройки
    static Boolean getOff;
    static Boolean quit;
    static Boolean death;
    static Boolean damage;
    private static Integer cooldownTime;

    //Сообщения
    static String horse;
    public static String noPermission;
    static String usage;
    static String onCooldown;
    public static String menuTitle;
    private static String duplicateHorse;
    public static String whereHorse;

    //Прочее
    static Boolean enableSettings;
    static Boolean enableCooldown;
    public static Main instance;
    public static Map<Player, Horse> horses = new HashMap<>();
    static Map<Player, Integer> cooldown = new HashMap<>();

    private static final BukkitScheduler bukkitScheduler = Bukkit.getScheduler();

    public void onEnable(){
        boolean enable = getConfig().getBoolean("main.ENABLE");
        if(!enable){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "JustHorse disabled in config.");
            return;
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "JustHorse v2.2 by SPY_me enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GuiMenu(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GuiListener(), this);
        instance = this;
        saveDefaultConfig();

        //Права
        permission = getConfig().getString("perm.USE_COMMAND", "justhorse.use");

        //Настройки
        enableSettings = getConfig().getBoolean("main.ENABLE_SETTINGS", true);
        enableCooldown = getConfig().getBoolean("main.ENABLE_COOLDOWN", true);
        getOff = getConfig().getBoolean("settings.REMOVE_ON_GET_OFF", true);
        quit = getConfig().getBoolean("settings.REMOVE_ON_PLAYER_QUIT", true);
        death = getConfig().getBoolean("settings.REMOVE_ON_DEATH", true);
        damage = getConfig().getBoolean("settings.CANCEL_DAMAGE", true);
        cooldownTime = getConfig().getInt("settings.COOLDOWN_TIME", 0);

        //Сообщения
        horse = getConfig().getString("msg.HORSE", "").replaceAll("&", "§");
        noPermission = getConfig().getString("msg.NO_PERMISSION", "").replaceAll("&", "§");
        usage = getConfig().getString("msg.USAGE", "").replaceAll("&", "§");
        onCooldown = getConfig().getString("msg.ON_COOLDOWN", "").replaceAll("&", "§");
        menuTitle = getConfig().getString("msg.MENU_TITLE", "").replaceAll("&", "§");
        duplicateHorse = getConfig().getString("msg.DUPLICATE_HORSE", "").replaceAll("&", "§");
        whereHorse = getConfig().getString("msg.WHERE_HORSE", "").replaceAll("&", "§");

        if(enableCooldown){
            timer(() -> {
                Map<Player, Integer> newCooldown = new HashMap<>();
                for(Map.Entry<Player, Integer> entry : cooldown.entrySet()){
                    Player player = entry.getKey();
                    Integer integer = entry.getValue();
                    if(integer != 0 && player != null){
                        newCooldown.put(player, integer - 1);
                    }
                }
                cooldown = newCooldown;
            });
        }
    }

    public void onDisable(){
        for(Map.Entry<Player, Horse> entry : horses.entrySet()){
            Player player = entry.getKey();
            Horse value = entry.getValue();
            value.remove();
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "JustHorse v2.2 by SPY_me disabled.");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args){
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

    public String getMessage(String key){
        return getConfig().getString("msg." + key.toUpperCase(), key).replaceAll("&", "§");
    }

    public String getPermission(String key){
        return getConfig().getString("perm." + key.toUpperCase());
    }

    static void spawnHorse(Player player){
        if(horses.containsKey(player)){
            player.sendMessage(Main.duplicateHorse);
            return;
        }
        Horse horse = createHorse(player);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        horse.getInventory().setArmor(null);
    }

    private static Horse createHorse(Player player){
        Horse horse = player.getWorld().spawn(player.getLocation(), Horse.class);
        horse.setStyle(Horse.Style.NONE);
        horse.setColor(Horse.Color.GRAY);
        horse.setAdult();

        horse.setCustomName(Main.horse + " " + player.getName());
        horse.setCustomNameVisible(true);

        horse.addPassenger(player);
        horse.setMaxDomestication(1);
        horse.setDomestication(1);
        horse.setCustomNameVisible(false);
        horses.put(player, horse);
        return horse;
    }

    static void addCooldown(Player player){
        cooldown.put(player, Main.cooldownTime);
    }

    /**
     * Запустить таймер
     *
     * @param runnable таск
     */
    private void timer(Runnable runnable){
        bukkitScheduler.runTaskTimer(this, runnable, 20, 20);
    }

    /**
     * Запустить таймер
     *
     * @param runnable таск
     */
    void later(Runnable runnable){
        bukkitScheduler.runTaskLater(this, runnable, 1);
    }
}
