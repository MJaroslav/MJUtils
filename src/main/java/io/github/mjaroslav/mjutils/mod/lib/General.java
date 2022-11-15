package io.github.mjaroslav.mjutils.mod.lib;


import io.github.mjaroslav.mjutils.config.annotations.Comment;
import io.github.mjaroslav.mjutils.config.annotations.Restart;

@Comment("Root option category, contains all options and subcategories.")
public class General {
    @Comment("Debug options for dev or issue hunt.")
    public static class Debug {
        @Comment("Draw special lines in GuiContentScrollingPane and in child elements.")
        public static boolean renderDebugLinesInScrollingPane = false;
    }

    @Comment("Cosmetic options, not make changes on server.")
    public static class Client {
        @Comment("Show ore dict names in tooltip (in advanced tooltip mode).")
        public static boolean showOreDictNames = true;

        @Comment("Show ore dict names always.")
        public static boolean alwaysShowOreDictNames = false;

        @Comment("Toggles of default GUIs replacements (for new features or/and fixes)")
        public static class GuiReplacements {
            @Comment("Allows you disable/enable mods, provides displaying of screenshots, etc in main menu mods GUI")
            public static boolean mainMenuModList = true;

            @Comment("Allows you disable/enable mods, provides displaying of screenshots, etc in pause menu mods GUI")
            public static boolean optionsModList = true;
        }
    }

    @Comment("Creative mode options")
    public static class Creative {
        @Comment("Disabling block breaking (same as swords in vanilla) for some items")
        public static class BlockBreaking {
            @Comment("All items with ItemSword parent class (same as vanilla)")
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

    @Comment("Add quartz ore to pigman trigger list")
    @Restart
    public static boolean quartzTrigger = true;

    @Comment("Drop all blocks on TNT explosion")
    public static boolean noLossTNTExplosion = true;
}
