package io.github.mjaroslav.mjutils.world;

import cpw.mods.fml.common.registry.GameRegistry;
import io.github.mjaroslav.mjutils.asm.mixin.accessors.AccessorFishingHooks;
import io.github.mjaroslav.mjutils.asm.mixin.accessors.AccessorWeightedRandomFishable;
import io.github.mjaroslav.mjutils.item.Stacks;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.FishingHooks.FishableCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.function.Predicate;

import static io.github.mjaroslav.mjutils.lib.MJUtilsInfo.*;

@UtilityClass
public class FishingHelper {
    public void addFishable(@NotNull ItemStack itemStack, @NotNull FishableCategory category, int weight,
                            boolean enchantable, @Range(from = 0, to = 1) float randomDamage) {
        LOG_LIB.debug("{} adds ItemStack {} with (weight={}, enchantable={}, randomDamage={}) parameters to {} " +
                "fishing category", UtilsMods.getActiveModNameFormatted(), itemStack, weight, enchantable, randomDamage,
            category);
        val wrf = new WeightedRandomFishable(itemStack, weight);
        if (enchantable) wrf.func_150707_a();
        wrf.func_150709_a(randomDamage);
        internal$addFishable(wrf, category);
    }

    public void removeFishable(@NotNull ItemStack itemStack, @NotNull FishableCategory category, boolean enchantable,
                               boolean damageable) {
        removeFishable(itemStack, category, Stacks.ITEM | Stacks.META_WILDCARD, enchantable, damageable);
    }

    public void removeFishable(@Nullable ItemStack itemStack, @NotNull FishableCategory category, int mask,
                               boolean enchantable, boolean damageable) {
        LOG_LIB.debug("{} removes ItemStack {} with (mask={}, enchantable={}, damageable={}) parameters from {} " +
                "fishing category", UtilsMods.getActiveModNameFormatted(), itemStack, mask, enchantable, damageable,
            category);
        internal$removeFishable(wrf -> {
            val accessor = ((AccessorWeightedRandomFishable) wrf);
            return Stacks.equals(accessor.getFishableStack(), itemStack, mask) && accessor.getEnchantable()
                == enchantable && (!damageable || accessor.getRandomDamage() > 0.0F);
        }, category);
    }

    public void removeFishable(@NotNull Block block, @NotNull FishableCategory category) {
        LOG_LIB.debug("{} removes Block {} from {} fishing category", UtilsMods.getActiveModNameFormatted(),
            GameRegistry.findUniqueIdentifierFor(block), category);
        internal$removeFishable(wrf -> ((AccessorWeightedRandomFishable) wrf).getFishableStack().getItem() ==
            Item.getItemFromBlock(block), category);
    }

    public void removeFishable(@NotNull Item item, @NotNull FishableCategory category) {
        LOG_LIB.debug("{} removes Item {} from {} fishing category", UtilsMods.getActiveModNameFormatted(),
            GameRegistry.findUniqueIdentifierFor(item), category);
        internal$removeFishable(wrf -> ((AccessorWeightedRandomFishable) wrf).getFishableStack().getItem() == item,
            category);
    }

    public @NotNull List<WeightedRandomFishable> getFishableCategory(@NotNull FishableCategory category) {
        return switch (category) {
            case FISH -> AccessorFishingHooks.getFish();
            case JUNK -> AccessorFishingHooks.getJunk();
            case TREASURE -> AccessorFishingHooks.getTreasure();
        };
    }

    public void clearFishableCategory(@NotNull FishableCategory category) {
        LOG_LIB.warn("{} clear {} fish category! It's ok if caller rewrites fishing loot of this category",
            UtilsMods.getActiveModNameFormatted(), category.name());
        getFishableCategory(category).clear();
    }

    public void clearAllCategories() {
        LOG_LIB.warn("{} clear all fish loot categories! It's ok if caller rewrites all fishing loot",
            UtilsMods.getActiveModNameFormatted());
        for (val category : FishableCategory.values()) getFishableCategory(category).clear();
    }

    private void internal$addFishable(@NotNull WeightedRandomFishable wrf, @NotNull FishableCategory category) {
        switch (category) {
            case FISH:
                FishingHooks.addFish(wrf);
                break;
            case JUNK:
                FishingHooks.addJunk(wrf);
            case TREASURE:
                FishingHooks.addTreasure(wrf);
        }
    }

    private void internal$removeFishable(@NotNull Predicate<WeightedRandomFishable> test,
                                         @NotNull FishableCategory category) {
        switch (category) {
            case FISH -> FishingHooks.removeFish(test::test);
            case JUNK -> FishingHooks.removeJunk(test::test);
            case TREASURE -> FishingHooks.removeTreasure(test::test);
        }
    }
}
