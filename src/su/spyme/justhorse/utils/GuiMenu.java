package su.spyme.justhorse.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Гуи меню
 */
public class GuiMenu implements Listener {
    private Player player; //Игрок
    private Inventory inventory; //Инвентарь
    private final Set<GuiItem> items = new HashSet<>(); //Предметы

    /**
     * Создание гуи
     * Для категорий (String key, User user, String nameCategory)
     * Открывать категорию, через guiOpenCategoryMenuItem. Куда передают @key, @user, @nameCategory.
     * Пример регистрации категорий, в BuildBattlePlugin.class
     *
     * @param player игрок
     * @param size   1-6
     * @param title  название гуи
     */
    public GuiMenu(Player player, int size, String title) {
        this.player = player;
        this.inventory = Bukkit.createInventory(null, size * 9, title.length() > 32 ? title.substring(0, 32) : title);
    }

    public GuiMenu() {

    }

    public static int getWidth(InventoryType type) {
        switch (type) {
            case CHEST:
            case PLAYER:
            case ENDER_CHEST:
            case SHULKER_BOX:
                return 9;
            case DISPENSER:
            case DROPPER:
            case WORKBENCH:
                return 3;
            case HOPPER:
                return 5;
            default:
                return -1;
        }
    }

    public static void openGui(GuiMenu gui) {
        gui.openOwner();
    }

    private void openOwner() {
        if (!this.isOpened()) {
            player.openInventory(inventory);
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        InventoryAction action = event.getAction();
        if (Objects.equals(event.getClickedInventory(), this.getInventory()) || !(action.equals(InventoryAction.PICKUP_ALL) || action.equals(InventoryAction.PLACE_ALL))) {
            event.setCancelled(true);
        }
        ItemStack item = event.getCurrentItem();
        if (item == null || event.getClick() == ClickType.DOUBLE_CLICK) {
            return;
        }
        for (GuiItem guiItem : items) {
            if (guiItem.equals(item)) {
                guiItem.click(event);
                break;
            }
        }
    }

    protected <T extends GuiItem> void addItem(T item) {
        items.add(item);
        item.update();
    }

    Collection<GuiItem> getItems() {
        return items;
    }

    Inventory getInventory() {
        return this.inventory;
    }

    private int getSize() {
        return inventory.getSize();
    }

    private boolean isOpened() {
        return player.getOpenInventory().getTopInventory().equals(this.getInventory());
    }

    /**
     * Узнать ширину инвентаря
     *
     * @return ширина (или -1, если инвентарь не квадратной формы)
     */
    int getWidth() {
        return getWidth(inventory.getType());
    }

    int getHeight() {
        return this.getSize() / this.getWidth();
    }
}
