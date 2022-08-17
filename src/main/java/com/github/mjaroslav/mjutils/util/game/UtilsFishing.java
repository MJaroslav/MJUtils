package com.github.mjaroslav.mjutils.util.game;

import com.github.mjaroslav.mjutils.asm.mixin.AccessorFishingHooks;
import com.github.mjaroslav.mjutils.asm.mixin.AccessorWeightedRandomFishable;
import com.github.mjaroslav.mjutils.util.game.item.UtilsItemStack;
import com.github.mjaroslav.mjutils.util.game.item.UtilsItemStack.CompareParameter;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.FishingHooks.FishableCategory;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;
import java.util.List;

/**
 * A set of tools to change the list of fishing catch.
 */
@SuppressWarnings({"Guava"}) // TODO: Try not use guava?
public class UtilsFishing {
    /**
     * Add new item to fishing category.
     *
     * @param fishable item to add.
     * @param category fishing category for modification.
     */
    public static void add(WeightedRandomFishable fishable, FishableCategory category) {
        switch (category) { // Trying use original cache
            case FISH:
                FishingHooks.addFish(fishable);
                break;
            case JUNK:
                FishingHooks.addJunk(fishable);
            case TREASURE:
                FishingHooks.addTreasure(fishable);
        }
    }

    /**
     * Add new item to fishing category.
     *
     * @param itemStack    item stack to add.
     * @param category     fishing category for modification.
     * @param weight       weight of item in randomizer.
     * @param enchantable  item will be randomly enchanted.
     * @param randomDamage item will be randomly damaged.
     */
    public static void add(ItemStack itemStack, FishableCategory category, int weight, boolean enchantable,
                           float randomDamage) {
        WeightedRandomFishable fishable = new WeightedRandomFishable(itemStack, weight);
        if (enchantable)
            fishable.func_150707_a();
        fishable.func_150709_a(randomDamage);
        add(fishable, category);
    }

    /**
     * Remove item from fishing category.
     *
     * @param fishable pattern to remove.
     * @param category fishing category for modification.
     */
    public static void remove(WeightedRandomFishable fishable, FishableCategory category) {
        remove(Predicates.not(new PredicateItemStacksEquals(fishable)), category);
    }

    /**
     * Remove item from fishing category.
     *
     * @param itemStack    item stack to remove.
     * @param category     fishing category for modification.
     * @param enchantable  item randomly enchanted?
     * @param randomDamage item randomly damaged?
     */
    public static void remove(ItemStack itemStack, FishableCategory category, boolean enchantable,
                              float randomDamage) {
        remove(Predicates.not(new PredicateItemStacksEquals(itemStack, enchantable, randomDamage)), category);
    }

    /**
     * Remove all block variants from specified fishing category.
     *
     * @param block    block to remove.
     * @param category fishing category for modification.
     */
    public static void remove(Block block, FishableCategory category) {
        remove(Predicates.not(new PredicateItemTypesEquals(block)), category);
    }

    /**
     * Remove all item variants from specified fishing category.
     *
     * @param item     item to remove.
     * @param category fishing category for modification.
     */
    public static void remove(Item item, FishableCategory category) {
        remove(Predicates.not(new PredicateItemTypesEquals(item)), category);
    }

    /**
     * Remove items from fishing category by filter.
     *
     * @param test     filter for remover.
     * @param category fishing category for modification.
     */
    public static void remove(Predicate<WeightedRandomFishable> test, FishableCategory category) {
        switch (category) { // Trying use original cache
            case FISH:
                FishingHooks.removeFish(test);
                break;
            case JUNK:
                FishingHooks.removeJunk(test);
                break;
            case TREASURE:
                FishingHooks.removeTreasure(test);
        }
    }

    /**
     * Get values of fishing category
     *
     * @param category specified fishing category.
     * @return Set of fishing category values.
     */
    public static List<WeightedRandomFishable> getCategory(FishableCategory category) {
        switch (category) {
            case FISH:
                return AccessorFishingHooks.getFish();
            case JUNK:
                return AccessorFishingHooks.getJunk();
            case TREASURE:
                return AccessorFishingHooks.getTreasure();
            default:
                return Collections.emptyList();
        }
    }

    /**
     * Remove all values from fishing category.
     *
     * @param category specified category.
     */
    public static void clear(FishableCategory category) {
        getCategory(category).clear();
    }

    /**
     * Remove all values from all fishing categories.
     */
    public static void clearAll() {
        for (FishableCategory category : FishableCategory.values())
            clear(category);
    }

    private static class PredicateItemStacksEquals implements Predicate<WeightedRandomFishable> {
        private final ItemStack pattern;
        private final float randomDamage;
        private final boolean enchantable;

        private PredicateItemStacksEquals(WeightedRandomFishable fishable) {
            pattern = ((AccessorWeightedRandomFishable) fishable).getFishableStack().copy();
            randomDamage = ((AccessorWeightedRandomFishable) fishable).getRandomDamage();
            enchantable = ((AccessorWeightedRandomFishable) fishable).getEnchantable();
        }

        private PredicateItemStacksEquals(ItemStack pattern, boolean enchantable, float randomDamage) {
            this.pattern = pattern.copy();
            this.randomDamage = randomDamage;
            this.enchantable = enchantable;
        }

        @Override
        public boolean apply(WeightedRandomFishable input) {
            if (input == null)
                return false;
            return UtilsItemStack.isEquals(((AccessorWeightedRandomFishable) input).getFishableStack(), pattern, CompareParameter.ITEM, CompareParameter.META, CompareParameter.COUNT) &&
                    ((AccessorWeightedRandomFishable) input).getEnchantable() == enchantable && ((AccessorWeightedRandomFishable) input).getRandomDamage() == randomDamage;
        }
    }

    private static class PredicateItemTypesEquals implements Predicate<WeightedRandomFishable> {
        private final ItemStack pattern;

        private PredicateItemTypesEquals(Block block) {
            pattern = new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
        }

        private PredicateItemTypesEquals(Item item) {
            pattern = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
        }

        @Override
        public boolean apply(WeightedRandomFishable input) {
            if (input == null)
                return false;
            return UtilsItemStack.isEquals(((AccessorWeightedRandomFishable) input).getFishableStack(), pattern, CompareParameter.ITEM, CompareParameter.META);
        }
    }
}
