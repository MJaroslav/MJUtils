package mjaroslav.mcmods.mjutils.mod.lib;

import mjaroslav.mcmods.mjutils.lib.module.ConfigCategory;

@ConfigCategory(modid = ModInfo.MODID, name = ConfigCommonInfo.CATEGORY, requiresMcRestart = true, comment = "Category for server settings and those that require restarting the game.")
public class ConfigCommonInfo {
    /**
     * Category for server settings and those that require restarting the game.
     */
    public static final String CATEGORY = ConfigGeneralInfo.CATEGORY + ".common";
}
