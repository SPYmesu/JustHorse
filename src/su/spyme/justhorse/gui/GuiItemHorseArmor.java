package su.spyme.justhorse.gui;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import su.spyme.justhorse.Main;
import su.spyme.justhorse.utils.GuiItem;
import su.spyme.justhorse.utils.GuiMenu;


public class GuiItemHorseArmor extends GuiItem implements Listener {

    private final String perm;
    private final Material material;

    GuiItemHorseArmor(GuiMenu guiMenu, int x, int y, Material material, String name) {
        super(guiMenu, x, y, material, 1);
        this.perm = Main.instance.getPermission("armor_" + name);
        String deny = Main.instance.getMessage("armor_deny_" + name);
        name = Main.instance.getMessage("armor_" + name);
        this.material = material;

        StringBuilder builder = new StringBuilder(Main.instance.getMessage("armor").replace("%name%", name));

        if (!deny.isEmpty()) {
            builder.append("\n§r\n").append(deny);
        }

        this.setText(builder.toString());
        GuiListener.guiItems.put(getItem(), this);
    }

    @EventHandler
    public void click(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Horse horse = Main.horses.get(player);
        if (horse == null) {
            player.sendMessage(Main.whereHorse);
        } else if (player.hasPermission(this.perm)) {
            if (this.material != Material.BARRIER) {
                horse.getInventory().setArmor(new ItemStack(this.material));
            } else {
                horse.getInventory().setArmor(null);
            }
        } else {
            player.sendMessage(Main.noPermission);
        }
    }
}
