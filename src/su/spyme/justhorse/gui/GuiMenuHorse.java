package su.spyme.justhorse.gui;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import su.spyme.justhorse.Main;
import su.spyme.justhorse.utils.GuiMenu;

/**
 * Гуи настройки лошади
 */

public class GuiMenuHorse extends GuiMenu {

    public GuiMenuHorse(Player player) {
        super(player, 3, Main.menuTitle);

        int x1 = 1;
        int x2 = 1;
        int x3 = 1;

        addItem(new GuiItemHorseColor(this, x1++, 1, Material.INK_SACK, (short) 8, Horse.Color.GRAY));
        addItem(new GuiItemHorseColor(this, x1++, 1, Material.INK_SACK, (short) 3, Horse.Color.BROWN));
        addItem(new GuiItemHorseColor(this, x1++, 1, Material.BRICK, (short) 0, Horse.Color.DARK_BROWN));
        addItem(new GuiItemHorseColor(this, x1++, 1, Material.INK_SACK, (short) 11, Horse.Color.CHESTNUT));
        addItem(new GuiItemHorseColor(this, x1++, 1, Material.INK_SACK, (short) 14, Horse.Color.CREAMY));
        addItem(new GuiItemHorseColor(this, x1++, 1, Material.INK_SACK, (short) 0, Horse.Color.BLACK));
        addItem(new GuiItemHorseColor(this, x1, 1, Material.INK_SACK, (short) 15, Horse.Color.WHITE));

        addItem(new GuiItemHorseStyle(this, x2++, 2, Material.BARRIER, (short) 0, Horse.Style.NONE));
        addItem(new GuiItemHorseStyle(this, x2++, 2, Material.MONSTER_EGG, (short) 0, Horse.Style.WHITEFIELD));
        addItem(new GuiItemHorseStyle(this, x2++, 2, Material.INK_SACK, (short) 15, Horse.Style.WHITE));
        addItem(new GuiItemHorseStyle(this, x2++, 2, Material.MELON_SEEDS, (short) 0, Horse.Style.BLACK_DOTS));
        addItem(new GuiItemHorseStyle(this, x2, 2, Material.PUMPKIN_SEEDS, (short) 0, Horse.Style.WHITE_DOTS));

        addItem(new GuiItemHorseArmor(this, x3++, 3, Material.BARRIER, (short) 0, "none"));
        addItem(new GuiItemHorseArmor(this, x3++, 3, Material.IRON_BARDING, (short) 0, "iron"));
        addItem(new GuiItemHorseArmor(this, x3++, 3, Material.GOLD_BARDING, (short) 0, "gold"));
        addItem(new GuiItemHorseArmor(this, x3, 3, Material.DIAMOND_BARDING, (short) 0, "diamond"));

    }

}
