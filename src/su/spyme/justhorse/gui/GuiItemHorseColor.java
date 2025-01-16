package su.spyme.justhorse.gui;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import su.spyme.justhorse.Main;
import su.spyme.justhorse.utils.GuiItem;
import su.spyme.justhorse.utils.GuiMenu;


public class GuiItemHorseColor extends GuiItem implements Listener {

    private final String perm;
    private final Horse.Color horseColor;

    GuiItemHorseColor(GuiMenu guiMenu, int x, int y, Material material, Horse.Color horseColor) {
        super(guiMenu, x, y, material, 1);
        this.perm = Main.instance.getPermission("color_" + horseColor.name());
        String name = Main.instance.getMessage("color_" + horseColor.name());
        String deny = Main.instance.getMessage("color_deny_" + horseColor.name());
        this.horseColor = horseColor;

        StringBuilder builder = new StringBuilder(Main.instance.getMessage("color").replace("%name%", name));

        if (!deny.isEmpty()) {
            builder.append("\n§r\n").append(deny);
        }

        this.setText(builder.toString());
        GuiListener.guiItems.put(getItem(), this);
    }

    @Override
    public void click(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Horse horse = Main.horses.get(player);
        if (horse == null) {
            player.sendMessage(Main.whereHorse);
        } else if (player.hasPermission(this.perm)) {
            horse.setColor(this.horseColor);
        } else {
            player.sendMessage(Main.noPermission);
        }
    }
}
