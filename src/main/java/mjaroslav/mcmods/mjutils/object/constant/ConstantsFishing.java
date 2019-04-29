package mjaroslav.mcmods.mjutils.object.constant;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ConstantsFishing {
    public static final ItemStack DEFAULT_FISH = new ItemStack(Items.fish, 1, 0);
    public static final ItemStack DEFAULT_JUNK = new ItemStack(Items.stick, 1, 0);
    public static final ItemStack DEFAULT_TREASURE = new ItemStack(Items.name_tag, 1, 0);

    public static final int RARITY_FISH = 60;
    public static final int RARITY_JUNK = 10;
    public static final int RARITY_TREASURE = 1;
}
