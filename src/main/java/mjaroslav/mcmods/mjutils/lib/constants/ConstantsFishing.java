package mjaroslav.mcmods.mjutils.lib.constants;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ConstantsFishing {
    /**
     * Default ItemStack for FISH list.
     */
    public static final ItemStack defaultFish = new ItemStack(Items.fish, 1, 0);
    /**
     * Default ItemStack for JUNK list.
     */
    public static final ItemStack defaultJunk = new ItemStack(Items.stick, 1, 0);
    /**
     * Default ItemStack for TREASURE list.
     */
    public static final ItemStack defaultTreasure = new ItemStack(Items.name_tag, 1, 0);
    /**
     * Default weight (rarity) for FISH list.
     */
    public static final int rarityFish = 60;
    /**
     * Default weight (rarity) for JUNK list.
     */
    public static final int rarityJunk = 10;
    /**
     * Default weight (rarity) for TREASURE list.
     */
    public static final int rarityTreasure = 1;
}
