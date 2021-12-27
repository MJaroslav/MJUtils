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
        @DefaultInt(0)
        @Comment("I'm a ranged int option!")
        public static int exampleInt;

        @Comment("I'm a string list option!")
        @DefaultStringArray({"hello", "world"})
        public static String[] exampleStrings;

        @Comment("I'm a color option!")
        @ColorType
        @DefaultString("c")
        public static String exampleColor;

        @Comment("I'm a color list option!")
        @ColorType
        @DefaultStringArray("c")
        public static String[] exampleColors;

        @Comment("I'm a mod id option!")
        @ModIdType
        @DefaultString("mjutilsexample")
        public static String exampleModId;
    }
}
