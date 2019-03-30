package su.spyme.moonhorse.gui;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import su.spyme.moonhorse.Main;
import su.spyme.moonhorse.utils.GuiMenu;

/**
 * Гуи настройки лошади
 */

public class GuiMenuHorse extends GuiMenu{

    public GuiMenuHorse(Player player){
        super(player, 3, Main.menuTitle);

        int x1 = 1;
        int x2 = 1;
        int x3 = 1;

        addItem(new GuiItemHorseColor(this, x1++, 1, Material.GRAY_DYE, Horse.Color.GRAY));
        addItem(new GuiItemHorseColor(this, x1++, 1, Material.COCOA_BEANS, Horse.Color.BROWN));
        addItem(new GuiItemHorseColor(this, x1++, 1, Material.BRICK, Horse.Color.DARK_BROWN));
        addItem(new GuiItemHorseColor(this, x1++, 1, Material.DANDELION_YELLOW, Horse.Color.CHESTNUT));
        addItem(new GuiItemHorseColor(this, x1++, 1, Material.ORANGE_DYE, Horse.Color.CREAMY));
        addItem(new GuiItemHorseColor(this, x1++, 1, Material.INK_SAC, Horse.Color.BLACK));
        addItem(new GuiItemHorseColor(this, x1, 1, Material.BONE_MEAL, Horse.Color.WHITE));

        addItem(new GuiItemHorseStyle(this, x2++, 2, Material.BARRIER, Horse.Style.NONE));
        addItem(new GuiItemHorseStyle(this, x2++, 2, Material.GHAST_SPAWN_EGG, Horse.Style.WHITEFIELD));
        addItem(new GuiItemHorseStyle(this, x2++, 2, Material.BONE_MEAL, Horse.Style.WHITE));
        addItem(new GuiItemHorseStyle(this, x2++, 2, Material.MELON_SEEDS, Horse.Style.BLACK_DOTS));
        addItem(new GuiItemHorseStyle(this, x2, 2, Material.PUMPKIN_SEEDS, Horse.Style.WHITE_DOTS));

        addItem(new GuiItemHorseArmor(this, x3++, 3, Material.BARRIER, "none"));
        addItem(new GuiItemHorseArmor(this, x3++, 3, Material.IRON_HORSE_ARMOR, "iron"));
        addItem(new GuiItemHorseArmor(this, x3++, 3, Material.GOLDEN_HORSE_ARMOR, "gold"));
        addItem(new GuiItemHorseArmor(this, x3, 3, Material.DIAMOND_HORSE_ARMOR, "diamond"));

    }

}
