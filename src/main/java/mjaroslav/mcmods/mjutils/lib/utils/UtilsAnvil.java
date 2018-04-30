package mjaroslav.mcmods.mjutils.lib.utils;

import java.util.*;
import java.util.Map.Entry;

import mjaroslav.mcmods.mjutils.lib.handler.AnvilRecipe;
import net.minecraft.item.ItemStack;

public class UtilsAnvil {
    private static Map<AnvilRecipe, ItemStack> recipes = new HashMap<>();

    public static Map<AnvilRecipe, ItemStack> getRecipes() {
        return recipes;
    }

    public static void addRecipe(ItemStack result, AnvilRecipe recipe) {
        recipes.put(recipe, result);
    }

    public static AnvilRecipe getRecipe(ItemStack left, ItemStack right, String name, int cost) {
        Iterator iterator = recipes.entrySet().iterator();
        Entry entry;
        do {
            if (!iterator.hasNext())
                return null;
            entry = (Entry) iterator.next();
        } while (!((AnvilRecipe) entry.getKey()).equals(left, right, name, cost));
        return ((AnvilRecipe) entry.getKey());
    }

    public static ItemStack getResult(ItemStack left, ItemStack right, String name) {
        Iterator iterator = recipes.entrySet().iterator();
        Entry entry;
        do {
            if (!iterator.hasNext())
                return null;
            entry = (Entry) iterator.next();
        } while (!((AnvilRecipe) entry.getKey()).equals(left, right, name, -1));
        return (ItemStack) entry.getValue();
    }

    public static ItemStack getResult(AnvilRecipe recipe) {
        Iterator iterator = recipes.entrySet().iterator();
        Entry entry;
        do {
            if (!iterator.hasNext())
                return null;
            entry = (Entry) iterator.next();
        } while (!((AnvilRecipe) entry.getKey()).equals(recipe));
        return (ItemStack) entry.getValue();
    }
}
