package com.github.mjaroslav.mjutils.mod.lib;

import com.github.mjaroslav.mjutils.configurator.impl.configurator.forge.AnnotationForgeConfigurator.Comment;
import com.github.mjaroslav.mjutils.configurator.impl.configurator.forge.AnnotationForgeConfigurator.DefaultBoolean;
import com.github.mjaroslav.mjutils.configurator.impl.configurator.forge.AnnotationForgeConfigurator.Name;
import com.github.mjaroslav.mjutils.configurator.impl.configurator.forge.AnnotationForgeConfigurator.RequiresMCRestart;
import com.github.mjaroslav.mjutils.configurator.impl.loader.AnnotationForgeConfiguratorsLoader.AnnotationConfiguratorsLoaderMarker;

// TODO: Добавить выключатели для некоторых особенностей библиотеки.
@AnnotationConfiguratorsLoaderMarker(modId = ModInfo.MOD_ID, fileName = ModInfo.MOD_ID)
@Name("general")
@Comment("Root option category, contains all options and subcategories.")
public class CategoryGeneral {
    @Name("client")
    @Comment("Cosmetic options, not make changes on server.")
    public static class CategoryClient {
        @Comment("Show ore dict names in tooltip (in advanced tooltip mode).")
        @DefaultBoolean(true)
        public static boolean showOreDictNames;

        @Comment("Show ore dict names always.")
        @DefaultBoolean(false)
        public static boolean alwaysShowOreDictNames;
    }

    // TODO: Сделать поддержку изменения параметра без необходимости рестарта.
    @Comment("Add quartz ore to pigman trigger list.")
    @RequiresMCRestart
    @DefaultBoolean(true)
    public static boolean quartzTrigger;
}
