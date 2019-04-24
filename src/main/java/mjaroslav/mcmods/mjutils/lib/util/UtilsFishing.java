package mjaroslav.mcmods.mjutils.lib.util;

import static cpw.mods.fml.relauncher.ReflectionHelper.*;

import java.util.ArrayList;

import com.google.common.base.Predicates;

import mjaroslav.mcmods.mjutils.lib.constant.ConstantsFishing;
import mjaroslav.mcmods.mjutils.lib.object.other.FishableStackEqualTo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraftforge.common.FishingHooks;
import net.minecraftforge.common.FishingHooks.FishableCategory;

public class UtilsFishing {
    public static void removeItemFromAllCategories(ItemStack itemStack, boolean checkOnEmpty) {
        removeItemFromCategory(itemStack, FishableCategory.FISH, checkOnEmpty);
        removeItemFromCategory(itemStack, FishableCategory.JUNK, checkOnEmpty);
        removeItemFromCategory(itemStack, FishableCategory.TREASURE, checkOnEmpty);
    }

    public static void removeItemFromCategory(ItemStack itemStack, FishableCategory category, boolean checkOnEmpty) {
        switch (category) {
        case FISH: {
            FishingHooks.removeFish(Predicates.not(new FishableStackEqualTo(itemStack)));
            if (checkOnEmpty && getCategory(category).size() <= 0)
                addItemToCategory(ConstantsFishing.defaultFish, ConstantsFishing.rarityFish, category);
        }
            break;
        case JUNK: {
            FishingHooks.removeJunk(Predicates.not(new FishableStackEqualTo(itemStack)));
            if (checkOnEmpty && getCategory(category).size() <= 0)
                addItemToCategory(ConstantsFishing.defaultJunk, ConstantsFishing.rarityJunk, category);
        }
            break;
        case TREASURE: {
            FishingHooks.removeTreasure(Predicates.not(new FishableStackEqualTo(itemStack)));
            if (checkOnEmpty && getCategory(category).size() <= 0)
                addItemToCategory(ConstantsFishing.defaultTreasure, ConstantsFishing.rarityTreasure, category);
        }
            break;
        }
    }

    public static void addItemToCategory(ItemStack itemStack, int weight, FishableCategory category) {
        switch (category) {
        case FISH:
            FishingHooks.addFish(new WeightedRandomFishable(itemStack, weight));
            break;
        case JUNK:
            FishingHooks.addJunk(new WeightedRandomFishable(itemStack, weight));
            break;
        case TREASURE:
            FishingHooks.addTreasure(new WeightedRandomFishable(itemStack, weight));
        }
    }

    public static ArrayList<WeightedRandomFishable> getCategory(FishableCategory category) {
        try {
            switch (category) {
            case FISH:
                return (ArrayList<WeightedRandomFishable>) ((ArrayList<WeightedRandomFishable>) getPrivateValue(
                        FishingHooks.class, new FishingHooks(), "fish")).clone();
            case JUNK:
                return (ArrayList<WeightedRandomFishable>) ((ArrayList<WeightedRandomFishable>) getPrivateValue(
                        FishingHooks.class, new FishingHooks(), "junk")).clone();
            case TREASURE:
                return (ArrayList<WeightedRandomFishable>) ((ArrayList<WeightedRandomFishable>) getPrivateValue(
                        FishingHooks.class, new FishingHooks(), "treasure")).clone();
            }
        } catch (Exception e) {
        }
        return new ArrayList<WeightedRandomFishable>();
    }

    public static void clearCategory(FishableCategory category, boolean addDefaults) {
        for (WeightedRandomFishable fishable : getCategory(category))
            removeItemFromCategory(fishable.field_150711_b, category, false);
        if (addDefaults)
            switch (category) {
            case FISH:
                addItemToCategory(ConstantsFishing.defaultFish, ConstantsFishing.rarityFish, category);
                break;
            case JUNK:
                addItemToCategory(ConstantsFishing.defaultJunk, ConstantsFishing.rarityJunk, category);
                break;
            case TREASURE:
                addItemToCategory(ConstantsFishing.defaultTreasure, ConstantsFishing.rarityTreasure, category);
                break;
            }
    }

    public static void clearAllCategories(boolean addDefaults) {
        clearCategory(FishableCategory.FISH, addDefaults);
        clearCategory(FishableCategory.JUNK, addDefaults);
        clearCategory(FishableCategory.TREASURE, addDefaults);
    }
}
