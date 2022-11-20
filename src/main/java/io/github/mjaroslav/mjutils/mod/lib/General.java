package io.github.mjaroslav.mjutils.mod.lib;


import io.github.mjaroslav.mjutils.config.annotations.*;

@Comment("Root option category, contains all options and subcategories.")
public class General {
    @Comment("Debug options for dev or issue hunt.")
    public static class Debug {
        @Comment("Draw special lines in GuiContentScrollingPane and in child elements.")
        public static boolean renderDebugLinesInScrollingPane = false;

        @Comment("Block collision highlighting")
        public static class BlockCollisionHighlighting {
            @Range(min = 0, max = 2)
            @Comment("Enable highlighting of block bounding boxes. Can cause problems if block creating it's own collision list with dynamic data such as entities and world.")
            public static Toggle enable = Toggle.DISABLE;
            @Range(min = 1)
            @Comment("Highlight zone range in blocks.")
            public static int range = 1;

            @HEX
            @Comment("Colors for lines of AABB in cyclic order.")
            public static int[] colorCycle = new int[]{0x0000FF, 0x00FF00, 0xFF00FF, 0xFFFF00, 0x00FFFF};

            @HEX
            @Comment("Color for lines of AABB with dead zones.")
            public static int deadColor = 0xFF0000;

            @Ignore
            public enum Toggle {
                DISABLE, CURSOR, CURSOR_SHIFT, PLAYER, PLAYER_SHIFT;

                public boolean isEnabled() {
                    return this != DISABLE;
                }

                public boolean isShift() {
                    return this == CURSOR_SHIFT || this == PLAYER_SHIFT;
                }

                public boolean isPlayer() {
                    return this == PLAYER || this == PLAYER_SHIFT;
                }

                public boolean isCursor() {
                    return this == CURSOR || this == CURSOR_SHIFT;
                }
            }
        }
    }

    @Comment("Cosmetic options, not make changes on server.")
    public static class Client {

        @Comment("Show ore dict names in tooltip (in advanced tooltip mode).")
        public static boolean showOreDictNames = true;

        @Comment("Show ore dict names always.")
        public static boolean alwaysShowOreDictNames = false;

        @Comment("Toggles of default GUIs replacements (for new features or/and fixes)")
        public static class GuiReplacements {
            @Comment("Replace standard buttons and text fields colors if their text is #HEX color.")
            public static boolean colors = true;

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

    @Comment("Changes each exploded by TNT block drop chance with this value, you can use -1 for use vanilla behaviour")
    @Range(min = -1, max = 1)
    @Name("override_tnt_explosion_drop_chance")
    public static double overrideTNTExplosionDropChance = 1;

    @Comment("Changes each exploded by creeper block drop chance with this value, you can use -1 for use vanilla behaviour. CodeChickenCore tweak for disable block breaking have more priority than this")
    @Range(min = -1, max = 1)
    public static double overrideCreeperExplosionDropChance = -1;
}
