package io.github.mjaroslav.mjutils.asm;

import io.github.mjaroslav.mjutils.config.annotations.*;
import io.github.mjaroslav.mjutils.config.annotations.Restart.Value;
import io.github.mjaroslav.mjutils.internal.data.IDManager.OccupiedPolicy;
import org.jetbrains.annotations.NotNull;

@Name(MixinPatches.CATEGORY_MIXINS)
@Restart(value = Value.BOTH)
@Comment("Core modding configurations, edit this if you have troubles with compatibility or want disable some feature. All accessors will loaded anyway")
public class MixinPatches {
    @Ignore
    public static final String CATEGORY_MIXINS = "mixins";

    @Comment("Patches of FishingHooks and EntityFishHook for manipulating with fishing items, fishing categories and fix null fishing result crash, also adds FishingSuccessEvent")
    public static boolean fishing = true;

    @Comment("Other mod extra patches")
    public static class Compatibility {
        @Comment("Enable mod patching")
        public static boolean enable = true;

        @Comment("Thaumcraft mod patches")
        public static class Thaumcraft {
            @Comment("Enable Thaumcraft patching")
            public static boolean enable = true;

            @Comment("Patches Thaumcraft#GuiResearchPopup for not showing popups of research copies")
            public static boolean hidePopupsOfResearchCopies = true;
        }
    }

    @Comment("Enchantment array extension")
    public static class Enchantments {
        @Comment("Patches of Enchantment for extending Enchantment IDs range")
        public static boolean enable = true;

        @Range(min = 256)
        @Comment("New Enchantment array size")
        public static int newArraySize = 1024;

        @Comment("What do when trying registering Enchantment with occupied ID, use AUTO for using enchantments.properties with all IDs.")
        public static @NotNull OccupiedPolicy occupiedPolicy = OccupiedPolicy.AUTO;
    }

    @Comment("Potion array extension")
    public static class Potions {
        @Comment("Patches of Potion for extending Potion IDs range")
        public static boolean enable = true;

        @Range(min = 32)
        @Comment("New Potion array size, max value are 127 by default, use with 'patch_nbt' for limit up to 2^31-1")
        public static int newArraySize = 1024;

        @Comment("Patches PotionEffect for storing Potion ID as Integer instead of Byte")
        @Name("patch_nbt")
        public static boolean patchNBT = true;

        @Comment("What do when trying registering Potion with occupied ID, use AUTO for using potions.properties with all IDs.")
        public static @NotNull OccupiedPolicy occupiedPolicy = OccupiedPolicy.AUTO;
    }
}
