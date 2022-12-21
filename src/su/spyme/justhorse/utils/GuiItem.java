package su.spyme.justhorse.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;

/**
 * Класс представляет предмет в инвентаре
 */
public class GuiItem{

    private String text; //отображаемый текст
    private int pos; //pos in inventory
    private ItemStack item; //Предметы на разном языке
    private final GuiMenu guiMenu; //Меню, в котором они расположены (или скрыты)
    private boolean enchantEffect = false; //эффект зачарования
    private final ItemFlag[] itemFlags = ItemFlag.values();

    private GuiItem(GuiMenu guiMenu, int x, int y, ItemStack item){
        this(guiMenu, x, y, item, "");
    }

    public GuiItem(GuiMenu guiMenu, int x, int y, Material material, int amount){
        this(guiMenu, x, y, new ItemStack(material, amount));
    }

    private GuiItem(GuiMenu guiMenu, int x, int y, ItemStack item, String text){
        this.text = text;
        this.guiMenu = guiMenu;
        this.set(x, y, item);
    }

    private void set(int x, int y, ItemStack item){
        if(x < 1 || y < 1 || x > this.guiMenu.getWidth() || y > this.guiMenu.getHeight()){
            throw new IndexOutOfBoundsException("x = " + x + " должен быть от 1 до " + this.guiMenu.getWidth() + ", " +
                    "y = " + y + " должен быть от 1 до " + this.guiMenu.getHeight());
        }
        this.pos = this.getPos(x, y);
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            meta.removeItemFlags(ItemFlag.values());
            meta.addItemFlags(this.itemFlags);
            item.setItemMeta(meta);
        }
        this.item = fixItem(item);
        this.setText(this.text);
        this.setEnchantEffect(this.enchantEffect);
    }

    private org.bukkit.inventory.ItemStack fixItem(org.bukkit.inventory.ItemStack item){
        Inventory inventory = Bukkit.createInventory(null, 9, "");
        if(item == null){
            return null;
        }
        inventory.setItem(0, item);
        item = inventory.getItem(0);
        return item;
    }

    boolean equals(ItemStack item){
        return equalsIgnoreAmount(this.item, item);
    }

    private static boolean equalsIgnoreAmount(ItemStack item1, ItemStack item2){
        if(isNullOrAir(item1) && isNullOrAir(item2)){
            if(item1.isSimilar(item2)){
                return true;
            }else{
                return item1.getType() == item2.getType() &&
                        item1.getItemMeta() == item2.getItemMeta() &&
                        Objects.equals(item1.getItemMeta(), item2.getItemMeta());
            }
        }else{
            return false;
        }
    }

    private static boolean isNullOrAir(ItemStack item){
        return item != null && item.getType() != Material.AIR;
    }

    /**
     * Выполнение кода при клике
     */
    public void click(InventoryClickEvent event){
    }

    /**
     * convert pos
     */
    private int getPos(int x, int y){
        x--;
        y--;
        return (y * this.guiMenu.getWidth()) + x;
    }

    /**
     * Сделать текст уникальным
     *
     * @param text текст
     * @return уникальный тест
     */
    private String makeUniqueText(String text){
        String edit = text;
        int count = 0;
        main:
        while(true){
            for(GuiItem item : this.guiMenu.getItems()){
                if(edit != null && !item.equals(this) && edit.equals(item.getText())){
                    edit = text + getIntegerInColorCode(count++);
                    continue main;
                }
            }
            break;
        }
        return edit;
    }

    private static String getIntegerInColorCode(int code){
        StringBuilder text = new StringBuilder();
        for(char c : String.valueOf(code).toCharArray()){
            text.append("§").append(c);
        }
        return text.toString();
    }

    private String getText(){
        return this.text;
    }

    /**
     * Установить текст предмету
     *
     * @param text текст
     */
    protected void setText(String text){
        this.text = text = this.makeUniqueText(text);
        setText(this.item, text);
        update();
    }

    private static void setText(ItemStack item, String text){
        if(item == null){
            return;
        }
        if(text != null){
            String[] data = text.split("(::|\n|\r)");
            ItemMeta meta = item.getItemMeta();
            if(meta != null){
                meta.setDisplayName(data[0]);
                meta.setLore(data.length > 1 ? Arrays.asList(data).subList(1, data.length) : null);
                item.setItemMeta(meta);
            }
        }else{
            ItemMeta meta = item.getItemMeta();
            if(meta != null){
                meta.setDisplayName(null);
                meta.setLore(null);
                item.setItemMeta(meta);
            }
        }
    }

    /**
     * Добавить или убрать эффект зачарования
     *
     * @param enchantEffect true - добавить, false - убрать
     */
    private void setEnchantEffect(boolean enchantEffect){
        this.enchantEffect = enchantEffect;
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            if(enchantEffect){
                meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            }else{
                meta.getEnchants().keySet().forEach(meta::removeEnchant);
            }
            item.setItemMeta(meta);
        }
        update();
    }

    /**
     * Обновить предмет
     */
    void update(){
        this.guiMenu.getInventory().setItem(pos, item);
    }

    protected ItemStack getItem(){
        return this.item;
    }
}
