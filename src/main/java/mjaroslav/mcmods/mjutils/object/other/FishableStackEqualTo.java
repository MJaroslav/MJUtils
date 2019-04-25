package mjaroslav.mcmods.mjutils.object.other;

import com.google.common.base.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;

public class FishableStackEqualTo implements Predicate<WeightedRandomFishable> {
    public ItemStack itemStack;

    public FishableStackEqualTo(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static boolean itemStackEquals(ItemStack first, ItemStack second) {
        return first != null && second != null && first.getItem() != null && second.getItem() != null
                && first.getItem() == second.getItem()
                && ((first.getMaxDamage() == 0 && second.getMaxDamage() == 0
                        && first.getItemDamage() == second.getItemDamage())
                        || (first.getMaxDamage() > 0 && second.getMaxDamage() > 0));
    }

    public ItemStack getInternalItem() {
        return itemStack;
    }

    @Override
    public boolean apply(WeightedRandomFishable input) {
        return itemStackEquals(itemStack, input.field_150711_b);
    }
}
