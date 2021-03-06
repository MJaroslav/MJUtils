package mjaroslav.mcmods.mjutils.object.constant;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Constants for {@link mjaroslav.mcmods.mjutils.util.UtilsFishing UtilsFishing}.
 *
 * @see mjaroslav.mcmods.mjutils.util.UtilsFishing
 */
public class ConstantsFishing {
    /**
     * Raw fish x1
     */
    public static final ItemStack DEFAULT_FISH = new ItemStack(Items.fish, 1, 0);
    /**
     * Stick x1
     */
    public static final ItemStack DEFAULT_JUNK = new ItemStack(Items.stick, 1, 0);
    /**
     * Name tag x1
     */
    public static final ItemStack DEFAULT_TREASURE = new ItemStack(Items.name_tag, 1, 0);

    public static final int RARITY_FISH = 60;
    public static final int RARITY_JUNK = 10;
    public static final int RARITY_TREASURE = 1;
}
