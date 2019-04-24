package mjaroslav.mcmods.mjutils.lib.util;

import net.minecraft.item.ItemStack;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

public class UtilsInventory {
    public static boolean itemStackNotNull(ItemStack stack) {
        return stack != null && stack.getItem() != null && stack.stackSize > 0;
    }

    public static boolean itemStacksEquals(ItemStack first, ItemStack second,
                                           boolean canBeNull, boolean withSize, boolean withDamage, boolean withNBT) {
        if (itemStackNotNull(first) && itemStackNotNull(second)) {
            return first.getItem() == second.getItem() && (!withSize || first.stackSize == second.stackSize) &&
                    (!withDamage || (first.getItemDamage() == WILDCARD_VALUE || second.getItemDamage() ==
                            WILDCARD_VALUE || first.getItemDamage() == second.getItemDamage())) &&
                    (!withNBT || (ItemStack.areItemStackTagsEqual(first, second)));
        }
        return canBeNull;

    }

    public static boolean itemStackTypeEquals(ItemStack first, ItemStack second, boolean canBeNull) {
        return itemStacksEquals(first, second, canBeNull, false, true, false);
    }
}
