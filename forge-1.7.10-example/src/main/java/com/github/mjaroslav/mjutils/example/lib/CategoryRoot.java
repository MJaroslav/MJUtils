package com.github.mjaroslav.mjutils.example.lib;

import com.github.mjaroslav.mjutils.configurator.AnnotationConfigurator.*;

@Name("root")
@Comment("I'm root category, I must be a parent for other options and categories!")
public class CategoryRoot {
    @Comment("I'm a config boolean option!")
    @DefaultBoolean(false)
    public static boolean exampleBoolean;

    @Name("subcategory")
    @Comment("I'm a subcategory!")
    public static class SubCategory {
        @RequiresMCRestart
        @IntRange(max = 100, min = -100)
        @Comment("I'm a ranged int option!")
        public static int exampleInt;

        @Comment("I'm a color list option!")
        @ColorType
        public static String[] exampleColors;
    }
}
