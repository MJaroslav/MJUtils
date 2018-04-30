package mjaroslav.mcmods.mjutils.mod.lib;

import mjaroslav.mcmods.mjutils.lib.module.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

@ConfigCategory(modid = ModInfo.MODID, name = ConfigGeneralInfo.CATEGORY, comment = "Main options.")
public class ConfigGeneralInfo {
    public static final String CATEGORY = Configuration.CATEGORY_GENERAL;
}
