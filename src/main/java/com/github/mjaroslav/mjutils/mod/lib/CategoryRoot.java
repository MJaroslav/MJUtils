package com.github.mjaroslav.mjutils.mod.lib;

import com.github.mjaroslav.mjutils.configurator.AnnotationConfigurator.Comment;
import com.github.mjaroslav.mjutils.configurator.AnnotationConfigurator.DefaultBoolean;
import com.github.mjaroslav.mjutils.configurator.AnnotationConfigurator.Name;
import com.github.mjaroslav.mjutils.configurator.AnnotationConfigurator.RequiresMCRestart;

@Name("root")
@Comment("Root option category, contains all options and subcategories.")
public class CategoryRoot {
    public static final String configVersion = "0";

    @Name("debug")
    @Comment("Debug options for dev or issue hunt.")
    public static class CategoryDebug {
        @Comment("Draw special lines in GuiContentScrollingPane and in child elements.")
        @DefaultBoolean(false)
        public static boolean renderDebugLinesInScrollingPane;
    }

    @Name("client")
    @Comment("Cosmetic options, not make changes on server.")
    public static class CategoryClient {
        @Comment("Show ore dict names in tooltip (in advanced tooltip mode).")
        @DefaultBoolean(true)
        public static boolean showOreDictNames;

        @Comment("Show ore dict names always.")
        @DefaultBoolean(false)
        public static boolean alwaysShowOreDictNames;

        @Name("gui_replacements")
        @Comment("Toggles of default GUIs replacements (for new features or/and fixes)")
        public static class CategoryGuiReplacements {
            @Comment("Allows you disable/enable mods, provides displaying of screenshots, etc in main menu mods GUI")
            @DefaultBoolean(true)
            public static boolean mainMenuModList;

            @Comment("Allows you disable/enable mods, provides displaying of screenshots, etc in pause menu mods GUI")
            @DefaultBoolean(true)
            public static boolean optionsModList;
        }
    }

    @Name("creative")
    @Comment("Creative mode options")
    public static class CategoryCreative {
        @Name("block_breaking")
        @Comment("Disabling block breaking (same as swords in vanilla) for some items")
        public static class CategoryBlockBreaking {
            @Comment("All items with ItemSword parent class (same as vanilla)")
            @DefaultBoolean(true)
            public static boolean swords;

            @Comment("All items with ItemTool parent class (axe, pickaxe, shovel)")
            @DefaultBoolean(false)
            public static boolean tools;

            @Comment("All items with ItemAxe parent class (not required if 'tools' enabled)")
            @DefaultBoolean(false)
            public static boolean axes;

            @Comment("All items with ItemPickaxe parent class (not required if 'tools' enabled)")
            @DefaultBoolean(false)
            public static boolean pickaxes;

            @Comment("All items with ItemSpade (all shovels) parent class (not required if 'tools' enabled)")
            @DefaultBoolean(false)
            public static boolean shovels;

            @Comment("All items added by other mods")
            @DefaultBoolean(true)
            public static boolean extraItems;
        }
    }

    // TODO: Сделать поддержку изменения параметра без необходимости рестарта.
    @Comment("Add quartz ore to pigman trigger list")
    @RequiresMCRestart
    @DefaultBoolean(true)
    public static boolean quartzTrigger;

    @Comment("Drop all blocks on TNT explosion")
    @DefaultBoolean(false)
    public static boolean noLossTNTExplosion;
}
