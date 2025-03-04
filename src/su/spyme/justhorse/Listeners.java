package su.spyme.justhorse;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import su.spyme.justhorse.gui.GuiMenuHorse;
import su.spyme.justhorse.utils.GuiMenu;

import java.util.Map;

public class Listeners implements Listener {

    private final Map<Player, Horse> horses = Main.horses;

    @EventHandler
    public void onGetOff(VehicleExitEvent event) {
        Player player = (Player) event.getExited();
        if (horses.containsKey(player) && Main.getOff) {
            Horse horse = horses.get(player);
            Main.horses.remove(player);
            horse.remove();
            if (Main.enableCooldown) {
                Main.addCooldown(player);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (horses.containsKey(player) && Main.quit) {
            Horse horse = horses.get(player);
            Main.horses.remove(player);
            horse.remove();
            if (Main.enableCooldown) {
                Main.addCooldown(player);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if ((event.getEntity() instanceof Horse horse) && Main.death) {
            event.getDrops().clear();
            event.setDroppedExp(0);
            if (horse.getPassengers().isEmpty()) return;
            Player player = (Player) horse.getPassengers().getFirst();
            Main.horses.remove(player);
            horse.remove();
            if (Main.enableCooldown) {
                Main.addCooldown(player);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if ((event.getEntity() instanceof Horse horse) && Main.damage) {
            Player player = (Player) horse.getPassengers().getFirst();
            if (horses.containsKey(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if ((event.getEntity() instanceof Horse horse) && Main.damage) {
            Player player = (Player) horse.getPassengers().getFirst();
            if (horses.containsKey(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.getOpenInventory().getTitle().equals(Main.horse + " " + player.getName())) {
            if (Main.enableSettings) {
                event.setCancelled(true);
                GuiMenu.openGui(new GuiMenuHorse(player));
            }
        }
    }

    @EventHandler
    public void onInv(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        Main.instance.later(() -> {
            if (player.getOpenInventory().getTitle().equals(Main.horse + " " + player.getName()) && Main.enableSettings) {
                GuiMenu.openGui(new GuiMenuHorse(player));
            }
        });
    }


}
