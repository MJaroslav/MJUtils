package mjaroslav.mcmods.mjutils.common.fishing;

import com.google.common.base.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;

/**
 * Apply returns true if {@link FishableStackEqualTo#itemStack itemStack} equals
 * itemStack from input.
 *
 * @author MJaroslav
 */
public class FishableStackEqualTo implements Predicate<WeightedRandomFishable> {
    public ItemStack itemStack;

    /**
     * Container for first item.
     *
     * @param itemStack
     *            - internal itemStack. Used for
     *            {@link #apply(WeightedRandomFishable)}.
     */
    public FishableStackEqualTo(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Check equality between two itemStacks. Ignoring: NBT and damage (not
     * meta).
     *
     * @param first
     *            - first itemStack.
     * @param second
     *            - second itemStack.
     * @return True of itemStacks equals.
     */
    public static boolean itemStackEquals(ItemStack first, ItemStack second) {
        return first != null && second != null && first.getItem() != null && second.getItem() != null
                && first.getItem() == second.getItem()
                && ((first.getMaxDamage() == 0 && second.getMaxDamage() == 0
                        && first.getItemDamage() == second.getItemDamage())
                        || (first.getMaxDamage() > 0 && second.getMaxDamage() > 0));
    }

    /**
     * Get first item.
     *
     * @return First item (in this instance).
     */
    public ItemStack getInternalItem() {
        return itemStack;
    }

    @Override
    public boolean apply(WeightedRandomFishable input) {
        return itemStackEquals(itemStack, input.field_150711_b);
    }
}
