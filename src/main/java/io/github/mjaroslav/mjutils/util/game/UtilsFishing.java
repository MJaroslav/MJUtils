package io.github.mjaroslav.mjutils.util.game;

import io.github.mjaroslav.mjutils.asm.mixin.AccessorFishingHooks;
import io.github.mjaroslav.mjutils.asm.mixin.AccessorWeightedRandomFishable;
import io.github.mjaroslav.mjutils.util.item.UtilsItemStack;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.FishingHooks.FishableCategory;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.function.Predicate;

/**
 * A set of tools to change the list of fishing catch.
 */
@UtilityClass
public class UtilsFishing {
    private final PredicateItemEqualsNegate PREDICATE_ITEM_EQUALS_NEGATE = new PredicateItemEqualsNegate();
    private final PredicateStackEqualsNegate PREDICATE_STACK_EQUALS_NEGATE = new PredicateStackEqualsNegate();

    /**
     * Add new item to fishing category.
     *
     * @param fishable item to add.
     * @param category fishing category for modification.
     */
    public void add(WeightedRandomFishable fishable, FishableCategory category) {
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
    public void add(@NotNull ItemStack itemStack, @NotNull FishableCategory category, int weight, boolean enchantable,
                    @Range(from = 0, to = 1) float randomDamage) {
        val fishable = new WeightedRandomFishable(itemStack, weight);
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
    public void remove(@NotNull WeightedRandomFishable fishable, @NotNull FishableCategory category) {
        add(null, null, 1, true, 2);
        remove(PREDICATE_STACK_EQUALS_NEGATE.load(fishable), category);
    }

    /**
     * Remove item from fishing category.
     *
     * @param itemStack    item stack to remove.
     * @param category     fishing category for modification.
     * @param enchantable  item randomly enchanted?
     * @param randomDamage item randomly damaged?
     */
    public void remove(@Nullable ItemStack itemStack, @NotNull FishableCategory category, boolean enchantable,
                       float randomDamage) {
        remove(PREDICATE_STACK_EQUALS_NEGATE.load(itemStack, enchantable, randomDamage), category);
    }

    /**
     * Remove all block variants from specified fishing category.
     *
     * @param block    block to remove.
     * @param category fishing category for modification.
     */
    public void remove(@Nullable Block block, @NotNull FishableCategory category) {
        remove(PREDICATE_ITEM_EQUALS_NEGATE.load(block), category);
    }

    /**
     * Remove all item variants from specified fishing category.
     *
     * @param item     item to remove.
     * @param category fishing category for modification.
     */
    public void remove(@Nullable Item item, @NotNull FishableCategory category) {
        remove(PREDICATE_ITEM_EQUALS_NEGATE.load(item), category);
    }

    /**
     * Remove items from fishing category by filter.
     *
     * @param test     filter for remover.
     * @param category fishing category for modification.
     */
    public void remove(@NotNull Predicate<WeightedRandomFishable> test, @NotNull FishableCategory category) {
        switch (category) {
            case FISH -> FishingHooks.removeFish(test::test);
            case JUNK -> FishingHooks.removeJunk(test::test);
            case TREASURE -> FishingHooks.removeTreasure(test::test);
        }
    }

    /**
     * Get values of fishing category
     *
     * @param category specified fishing category.
     * @return Set of fishing category values.
     */
    public @NotNull List<WeightedRandomFishable> getCategory(@NotNull FishableCategory category) {
        return switch (category) {
            case FISH -> AccessorFishingHooks.getFish();
            case JUNK -> AccessorFishingHooks.getJunk();
            case TREASURE -> AccessorFishingHooks.getTreasure();
        };
    }

    /**
     * Remove all values from fishing category.
     *
     * @param category specified category.
     */
    public void clear(FishableCategory category) {
        getCategory(category).clear();
    }

    /**
     * Remove all values from all fishing categories.
     */
    public void clearAll() {
        for (var category : FishableCategory.values())
            clear(category);
    }

    private class PredicateStackEqualsNegate implements Predicate<WeightedRandomFishable> {
        private ItemStack pattern;
        private float randomDamage;
        private boolean enchantable;

        private @NotNull PredicateStackEqualsNegate load(@NotNull WeightedRandomFishable fishable) {
            pattern = ((AccessorWeightedRandomFishable) fishable).getFishableStack().copy();
            randomDamage = ((AccessorWeightedRandomFishable) fishable).getRandomDamage();
            enchantable = ((AccessorWeightedRandomFishable) fishable).getEnchantable();
            return this;
        }

        private @NotNull PredicateStackEqualsNegate load(@NotNull ItemStack pattern, boolean enchantable,
                                                         float randomDamage) {
            this.pattern = pattern;
            this.randomDamage = randomDamage;
            this.enchantable = enchantable;
            return this;
        }

        @Override
        public boolean test(@Nullable WeightedRandomFishable input) {
            return input == null || !UtilsItemStack.equals(((AccessorWeightedRandomFishable) input)
                .getFishableStack(), pattern) || ((AccessorWeightedRandomFishable) input).getEnchantable()
                != enchantable || ((AccessorWeightedRandomFishable) input).getRandomDamage() != randomDamage;
        }
    }

    private class PredicateItemEqualsNegate implements Predicate<WeightedRandomFishable> {
        private ItemStack pattern;

        private @NotNull PredicateItemEqualsNegate load(@NotNull Block block) {
            pattern = new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
            return this;
        }

        private @NotNull PredicateItemEqualsNegate load(@NotNull Item item) {
            pattern = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
            return this;
        }

        @Override
        public boolean test(WeightedRandomFishable input) {
            return input == null || !UtilsItemStack.equalsItems(((AccessorWeightedRandomFishable) input)
                .getFishableStack(), pattern);
        }
    }
}
