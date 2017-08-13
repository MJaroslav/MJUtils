package mjaroslav.mcmods.fishingcontroller.api;

import com.google.common.base.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;

/**
 * Apply returns true if {@link ItemStackEqualTo#itemStack itemStack} equals
 * itemStack from input
 * 
 * @author MJaroslav
 */
public class ItemStackEqualTo implements Predicate<WeightedRandomFishable> {
	
	public ItemStack itemStack;

	/**
	 * @param itemStack - Internal itemStack. Used for {@link #apply(WeightedRandomFishable)}.
	 */
	public ItemStackEqualTo(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public ItemStack getInternalItem() {
		return itemStack;
	}

	@Override
	public boolean apply(WeightedRandomFishable input) {
		return itemStackEquals(itemStack, input.field_150711_b);
	}

	/**
	 * Check equality between two itemStacks. Ignoring: NBT and damage (not
	 * meta)
	 * 
	 * @param first
	 *            - first itemStack
	 * @param second
	 *            - second itemStack
	 * @return true of itemStacks equals
	 */
	public static boolean itemStackEquals(ItemStack first, ItemStack second) {
		return first != null && second != null && first.getItem() != null && second.getItem() != null
				&& first.getItem() == second.getItem()
				&& ((first.getMaxDamage() == 0 && second.getMaxDamage() == 0
						&& first.getItemDamage() == second.getItemDamage())
						|| (first.getMaxDamage() > 0 && second.getMaxDamage() > 0));
	}
}
