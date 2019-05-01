package mjaroslav.mcmods.mjutils.mod.lib;

import mjaroslav.mcmods.mjutils.module.ConfigurationCategory;
import mjaroslav.mcmods.mjutils.module.ConfigurationProperty;

@ConfigurationCategory(modID = ModInfo.MODID, name = ConfigurationCategory.GENERAL_NAME,
        comment = ConfigurationCategory.GENERAL_COMMENT)
public class CategoryGeneral {
    @ConfigurationCategory(name = "client")
    public static class CategoryClient {
        @ConfigurationProperty(defaultBoolean = true, comment =
                "Show ore dict names in tooltip (in advanced tooltip mode).")
        public static boolean showOreDictNames;

        @ConfigurationProperty(defaultBoolean = true, comment =
                "Show ore dict names always.")
        public static boolean alwaysShowOreDictNames;
    }
}
