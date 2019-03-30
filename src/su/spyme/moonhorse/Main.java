package su.spyme.moonhorse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import su.spyme.moonhorse.gui.GuiListener;
import su.spyme.moonhorse.utils.GuiMenu;

import java.util.HashMap;
import java.util.Map;

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

    private static BukkitScheduler bukkitScheduler = Bukkit.getScheduler();

    public void onEnable(){
        boolean enable = getConfig().getBoolean("main.ENABLE");
        if(!enable){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MoonHorse disabled in config.");
            return;
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MoonHorse v2.1 by SPY_me enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GuiMenu(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GuiListener(), this);
        getCommand("horse").setExecutor(new CommandHorse());
        instance = this;
        saveDefaultConfig();

        //Права
        permission = getConfig().getString("perm.USE_COMMAND");

        //Настройки
        enableSettings = getConfig().getBoolean("main.ENABLE_SETTINGS");
        enableCooldown = getConfig().getBoolean("main.ENABLE_COOLDOWN");
        getOff = getConfig().getBoolean("settings.REMOVE_ON_GET_OFF");
        quit = getConfig().getBoolean("settings.REMOVE_ON_PLAYER_QUIT");
        death = getConfig().getBoolean("settings.REMOVE_ON_DEATH");
        damage = getConfig().getBoolean("settings.CANCEL_DAMAGE");
        cooldownTime = getConfig().getInt("settings.COOLDOWN_TIME");

        //Сообщения
        horse = getConfig().getString("msg.HORSE").replaceAll("&", "§");
        noPermission = getConfig().getString("msg.NO_PERMISSION").replaceAll("&", "§");
        usage = getConfig().getString("msg.USAGE").replaceAll("&", "§");
        onCooldown = getConfig().getString("msg.ON_COOLDOWN").replaceAll("&", "§");
        menuTitle = getConfig().getString("msg.MENU_TITLE").replaceAll("&", "§");
        duplicateHorse = getConfig().getString("msg.DUPLICATE_HORSE").replaceAll("&", "§");
        whereHorse = getConfig().getString("msg.WHERE_HORSE").replaceAll("&", "§");

        if(enableCooldown){
            timer(() -> {
                Map<Player, Integer> newCooldown = new HashMap<>();
                cooldown.forEach((player, integer) -> {
                    if(integer != 0 && player != null){
                        System.out.println(player.getName() + " " + integer);
                        newCooldown.put(player, integer - 1);
                    }
                });
                cooldown = newCooldown;
            });
        }
    }

    public void onDisable(){
        horses.forEach((player, horse) -> horse.remove());
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MoonHorse v2.1 by SPY_me disabled.");
    }

    public String getMessage(String key){
        return getConfig().getString("msg." + key.toUpperCase()).replaceAll("&", "§");
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
        System.out.println("adding " + player.getName());
        cooldown.put(player, Main.cooldownTime);
    }

    /**
     * Запустить таймер
     *
     * @param runnable таск
     */
    private void timer(Runnable runnable){
        bukkitScheduler.runTaskTimer(this, runnable, (long) 20, (long) 20);
    }

    /**
     * Запустить таймер
     *
     * @param runnable таск
     */
    void later(Runnable runnable){
        bukkitScheduler.runTaskLater(this, runnable, (long) 1);
    }
}
