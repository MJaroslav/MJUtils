package mjaroslav.mcmods.mjutils.mod.lib;

import mjaroslav.mcmods.mjutils.module.ConfigCategory;
import mjaroslav.mcmods.mjutils.module.ConfigField;

@ConfigCategory(modid = ModInfo.MODID, comment = "Main options.")
public class ConfigGeneralInfo {
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
