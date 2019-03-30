package su.spyme.moonhorse;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import su.spyme.moonhorse.gui.GuiMenuHorse;
import su.spyme.moonhorse.utils.GuiMenu;

import java.util.Map;

public class Listeners implements Listener{

    private Map<Player, Horse> horses = Main.horses;

    @EventHandler
    public void onGetOff(VehicleExitEvent event){
        Player player = (Player) event.getExited();
        if(horses.containsKey(player) && Main.getOff){
            Horse horse = horses.get(player);
            Main.horses.remove(player);
            horse.remove();
            if(Main.enableCooldown){
                Main.addCooldown(player);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(horses.containsKey(player) && Main.quit){
            Horse horse = horses.get(player);
            Main.horses.remove(player);
            horse.remove();
            if(Main.enableCooldown){
                Main.addCooldown(player);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event){
        if((event.getEntity() instanceof Horse) && Main.death){
            event.setCancelled(true);
            Horse horse = (Horse) event.getEntity();
            if(horse.getPassengers().isEmpty()) return;
            Player player = (Player) horse.getPassengers().get(0);
            Main.horses.remove(player);
            horse.remove();
            if(Main.enableCooldown){
                Main.addCooldown(player);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if((event.getEntity() instanceof Horse) && Main.damage){
            Horse horse = (Horse) event.getEntity();
            Player player = (Player) horse.getPassengers().get(0);
            if(horses.containsKey(player)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(player.getOpenInventory().getTitle().equals(Main.horse + " " + player.getName())){
            event.setCancelled(true);
            if(Main.enableSettings){
                GuiMenu.openGui(new GuiMenuHorse(player));
            }
        }
    }

    @EventHandler
    public void onInv(InventoryOpenEvent event){
        Player player = (Player) event.getPlayer();
        Main.instance.later(() -> {
            if(player.getOpenInventory().getTitle().equals(Main.horse + " " + player.getName()) && Main.enableSettings){
                GuiMenu.openGui(new GuiMenuHorse(player));
            }
        });
    }


}
