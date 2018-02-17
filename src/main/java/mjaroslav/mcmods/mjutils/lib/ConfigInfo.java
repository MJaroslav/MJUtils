package mjaroslav.mcmods.mjutils.lib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mjaroslav.mcmods.mjutils.common.objects.ConfigField;

public class ConfigInfo {
    /**
     * Category for server settings and those that require restarting the game.
     */
    public static final String CATEGORY_COMMON = "common";
    /**
     * Category for client settings and those that not require restarting the
     * game.
     */
    public static final String CATEGORY_CLIENT = "client";
    /**
     * Change chain mail armor to iron by iron ingot on anvil.
     */
    @ConfigField(defaultBoolean = true, category = CATEGORY_COMMON, requiresMcRestart = true, comment = "Change chainmail armor to iron by iron ingot on anvil.")
    public static boolean chainToIron;

    // COMMON FIELDS
    /**
     * Change leather armor to chain mail by 2 iron bars on anvil.
     */
    @ConfigField(defaultBoolean = true, category = CATEGORY_COMMON, requiresMcRestart = true, comment = "Change leather armor to chain mail by 2 iron bars on anvil.")
    public static boolean leatherToChain;
    // CLIENT FIELDS
    /**
     * Show ore dictionary names in tooltip (in advanced tooltip mode).
     */
    @ConfigField(defaultBoolean = true, category = CATEGORY_CLIENT, comment = "Show ore dict names in tooltip (in advanced tooltip mode).")
    public static boolean showOreDict;
    /**
     * Show ore dictionary names always.
     */
    @ConfigField(defaultBoolean = true, category = CATEGORY_CLIENT, comment = "Show ore dict names always.")
    public static boolean showOreDictAlways;
}
