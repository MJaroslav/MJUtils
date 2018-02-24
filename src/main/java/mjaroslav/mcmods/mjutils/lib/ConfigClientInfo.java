package mjaroslav.mcmods.mjutils.lib;

import mjaroslav.mcmods.mjutils.common.objects.ConfigCategory;
import mjaroslav.mcmods.mjutils.common.objects.ConfigField;

@ConfigCategory(modid = ModInfo.MODID, name = ConfigClientInfo.CATEGORY, comment = "Category for client settings and those that not require restarting the game.")
public class ConfigClientInfo {
    /**
     * Category for client settings and those that not require restarting the
     * game.
     */
    public static final String CATEGORY = ConfigGeneralInfo.CATEGORY + ".client";

    /**
     * Show ore dictionary names in tooltip (in advanced tooltip mode).
     */
    @ConfigField(defaultBoolean = true, comment = "Show ore dict names in tooltip (in advanced tooltip mode).")
    public static boolean showOreDict;
    /**
     * Show ore dictionary names always.
     */
    @ConfigField(defaultBoolean = true, comment = "Show ore dict names always.")
    public static boolean showOreDictAlways;
}
