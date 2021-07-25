package com.github.mjaroslav.mjutils.util;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

/**
 * A set of tools to work with ItemStacks and inventories.
 */
public class UtilsInventory {
    /**
     * Check ItemStack for suitability.
     *
     * @param stack ItemStack to check.
     * @return True of stack, item from stack
     * is not null and stack size more then 0
     */
    public static boolean itemStackNotNull(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() != null && stack.stackSize > 0;
    }

    /**
     * Check equality between two ItemStacks.
     *
     * @param first      first ItemStack to check.
     * @param second     second ItemStack to check.
     * @param canBeNull  if true, two nulls will return true.
     * @param withSize   if false, stack size will be ignored.
     * @param withDamage if false, item damage will be ignored.
     * @param withNBT    if false, item NBT data will be ignored.
     * @return True if stacks equals.
     */
    public static boolean itemStacksEquals(@Nullable ItemStack first, @Nullable ItemStack second,
                                           boolean canBeNull, boolean withSize, boolean withDamage, boolean withNBT) {
        if (itemStackNotNull(first) && itemStackNotNull(second)) {
            return first.getItem() == second.getItem() && (!withSize || first.stackSize == second.stackSize) &&
                    (!withDamage || (first.getItemDamage() == WILDCARD_VALUE || second.getItemDamage() ==
                            WILDCARD_VALUE || first.getItemDamage() == second.getItemDamage())) &&
                    (!withNBT || (ItemStack.areItemStackTagsEqual(first, second)));
        }
        return canBeNull;

    }

    /**
     * Check for equality between item and
     * metadata of ItemStack. Equivalent to
     * {@link UtilsInventory#itemStacksEquals(ItemStack, ItemStack,
     * boolean, boolean, boolean, boolean) itemStacksEquals(first,
     * second, canBeNull, false, true, false)}.
     *
     * @param first     first ItemStack to check.
     * @param second    second ItemStack to check.
     * @param canBeNull if true, two nulls will return true.
     * @return True if stacks equals.
     */
    public static boolean itemStackTypeEquals(@Nullable ItemStack first, @Nullable ItemStack second, boolean canBeNull) {
        return itemStacksEquals(first, second, canBeNull, false, true, false);
    }
}
