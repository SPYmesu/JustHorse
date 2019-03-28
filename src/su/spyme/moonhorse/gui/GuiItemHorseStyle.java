package su.spyme.moonhorse.gui;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import su.spyme.moonhorse.Main;
import su.spyme.moonhorse.utils.GuiItem;
import su.spyme.moonhorse.utils.GuiMenu;


public class GuiItemHorseStyle extends GuiItem implements Listener{

    private String perm;
    private Horse.Style horseStyle;

    GuiItemHorseStyle(GuiMenu guiMenu, int x, int y, Material material, short data, Horse.Style horseStyle){
        super(guiMenu, x, y, material, data, 1);
        this.perm = Main.instance.getPermission("style_" + horseStyle.name());
        String name = Main.instance.getMessage("style_" + horseStyle.name());
        String deny = Main.instance.getMessage("style_deny_" + horseStyle.name());
        this.horseStyle = horseStyle;
        StringBuilder builder = new StringBuilder(Main.instance.getMessage("style").replace("%name%", name));

        if(!deny.isEmpty()){
            builder.append("\n§r\n").append(deny);
        }

        this.setText(builder.toString());
        GuiListener.guiItems.put(getItem(), this);
    }

    @Override
    public void click(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Horse horse = Main.horses.get(player);
        if(horse == null){
            player.sendMessage(Main.whereHorse);
        }else if(player.hasPermission(this.perm)){
            horse.setStyle(this.horseStyle);
        }else{
            player.sendMessage(Main.noPermission);
        }
    }
}
