package mjaroslav.mcmods.mjutils.mod.lib;

import mjaroslav.mcmods.mjutils.module.ConfigurationCategory;
import mjaroslav.mcmods.mjutils.module.ConfigurationProperty;

// TODO: Добавить выключатели для некоторых особенностей библиотеки.
@ConfigurationCategory(modID = ModInfo.MOD_ID, name = ConfigurationCategory.GENERAL_NAME,
        comment = ConfigurationCategory.GENERAL_COMMENT)
public class CategoryGeneral {
    @ConfigurationCategory(name = "client")
    public static class CategoryClient {
        @ConfigurationProperty(defaultBoolean = true, comment =
                "Show ore dict names in tooltip (in advanced tooltip mode).")
        public static boolean showOreDictNames;

        @ConfigurationProperty(comment =
                "Show ore dict names always.")
        public static boolean alwaysShowOreDictNames;
    }

    // TODO: Сделать поддерку изменения параметра без необходимости рестарта.
    @ConfigurationProperty(defaultBoolean = true, requiresMcRestart = true, comment =
            "Add quartz ore to pigman trigger list.")
    public static boolean quartzTrigger;
}
