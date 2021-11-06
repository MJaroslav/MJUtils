package com.github.mjaroslav.mjutils.mod.lib;

import com.github.mjaroslav.mjutils.configurator.AnnotationConfigurator.Comment;
import com.github.mjaroslav.mjutils.configurator.AnnotationConfigurator.DefaultBoolean;
import com.github.mjaroslav.mjutils.configurator.AnnotationConfigurator.Name;
import com.github.mjaroslav.mjutils.configurator.AnnotationConfigurator.RequiresMCRestart;

@Name("general")
@Comment("Root option category, contains all options and subcategories.")
public class CategoryRoot {
    public static final String configVersion = "0";

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
