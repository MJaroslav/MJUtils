package mjaroslav.mcmods.mjutils.lib;

import mjaroslav.mcmods.mjutils.common.objects.ConfigCategory;
import mjaroslav.mcmods.mjutils.common.objects.ConfigField;

@ConfigCategory(modid = ModInfo.MODID, name = ConfigCommonInfo.CATEGORY, requiresMcRestart = true, comment = "Category for server settings and those that require restarting the game.")
public class ConfigCommonInfo {
    /**
     * Category for server settings and those that require restarting the game.
     */
    public static final String CATEGORY = ConfigGeneralInfo.CATEGORY + ".common";

    /**
     * Change chain mail armor to iron by iron ingot on anvil.
     */
    @ConfigField(defaultBoolean = true, comment = "Change chainmail armor to iron by iron ingot on anvil.")
    public static boolean chainToIron;

    /**
     * Change leather armor to chain mail by 2 iron bars on anvil.
     */
    @ConfigField(defaultBoolean = true, comment = "Change leather armor to chain mail by 2 iron bars on anvil.")
    public static boolean leatherToChain;
}
