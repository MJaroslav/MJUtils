package mjaroslav.mcmods.mjutils.mod.lib;

import mjaroslav.mcmods.mjutils.module.ConfigurationCategory;
import mjaroslav.mcmods.mjutils.module.ConfigurationProperty;

@ConfigurationCategory(modID = ModInfo.MODID, comment = "Main options.")
public class ConfigGeneralInfo {
    /**
     * Show ore dictionary names in tooltip (in advanced tooltip mode).
     */
    @ConfigurationProperty(defaultBoolean = true, comment = "Show ore dict names in tooltip (in advanced tooltip mode).")
    public static boolean showOreDict;
    /**
     * Show ore dictionary names always.
     */
    @ConfigurationProperty(defaultBoolean = true, comment = "Show ore dict names always.")
    public static boolean showOreDictAlways;
}
