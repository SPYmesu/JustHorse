package su.spyme.moonhorse.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import su.spyme.moonhorse.utils.GuiItem;

import java.util.HashMap;
import java.util.Map;

public class GuiListener implements Listener{

    static Map<ItemStack, GuiItem> guiItems = new HashMap<>();

    @EventHandler
    public void execute(InventoryClickEvent event){
        try{
            guiItems.get(event.getCurrentItem()).click(event);
            event.setCancelled(true);
        }catch(NullPointerException ignored){
        }
    }
}
