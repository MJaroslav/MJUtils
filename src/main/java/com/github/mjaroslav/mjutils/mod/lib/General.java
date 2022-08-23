package com.github.mjaroslav.mjutils.mod.lib;


import com.github.mjaroslav.mjutils.configurator.annotations.Comment;
import com.github.mjaroslav.mjutils.configurator.annotations.Default;
import com.github.mjaroslav.mjutils.configurator.annotations.Restart;
import com.github.mjaroslav.mjutils.configurator.annotations.Version;

@Comment("Root option category, contains all options and subcategories.")
@Version("0")
public class General {
    @Comment("Debug options for dev or issue hunt.")
    public static class Debug {
        @Comment("Draw special lines in GuiContentScrollingPane and in child elements.")
        public static boolean renderDebugLinesInScrollingPane = false;
    }

    @Comment("Cosmetic options, not make changes on server.")
    public static class Client {
        @Comment("Show ore dict names in tooltip (in advanced tooltip mode).")
        @Default(b = true)
        public static boolean showOreDictNames = true;

        @Comment("Show ore dict names always.")
        public static boolean alwaysShowOreDictNames = false;

        @Comment("Toggles of default GUIs replacements (for new features or/and fixes)")
        public static class GuiReplacements {
            @Comment("Allows you disable/enable mods, provides displaying of screenshots, etc in main menu mods GUI")
            @Default(b = true)
            public static boolean mainMenuModList = true;

            @Comment("Allows you disable/enable mods, provides displaying of screenshots, etc in pause menu mods GUI")
            @Default(b = true)
            public static boolean optionsModList = true;
        }
    }

    @Comment("Creative mode options")
    public static class Creative {
        @Comment("Disabling block breaking (same as swords in vanilla) for some items")
        public static class BlockBreaking {
            @Comment("All items with ItemSword parent class (same as vanilla)")
            @Default(b = true)
            public static boolean swords = true;

            @Comment("All items with ItemTool parent class (axe, pickaxe, shovel)")
            public static boolean tools = false;

            @Comment("All items with ItemAxe parent class (not required if 'tools' enabled)")
            public static boolean axes = false;

            @Comment("All items with ItemPickaxe parent class (not required if 'tools' enabled)")
            public static boolean pickaxes = false;

            @Comment("All items with ItemSpade (all shovels) parent class (not required if 'tools' enabled)")
            public static boolean shovels = false;

            @Comment("All items added by other mods")
            public static boolean extraItems = true;
        }
    }

    // TODO: Сделать поддержку изменения параметра без необходимости рестарта.
    @Comment("Add quartz ore to pigman trigger list")
    @Restart
    @Default(b = true)
    public static boolean quartzTrigger = true;

    @Comment("Drop all blocks on TNT explosion")
    @Default(b = true)
    public static boolean noLossTNTExplosion = true;
}
