package mjaroslav.mcmods.mjutils.common.anvil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

/**
 * Anvil recipe handler. You can add a recipe here.
 * 
 * @author MJaroslav
 *
 */
public class AnvilUtils {
	/**
	 * Anvil recipes map, use {@link AnvilUtils#getRecipes()}.
	 */
	private static Map<AnvilRecipe, ItemStack> recipes = new HashMap();

	/**
	 * Get anvil recipe map.
	 * 
	 * @return Recipe - result map.
	 */
	public static Map<AnvilRecipe, ItemStack> getRecipes() {
		return recipes;
	}

	/**
	 * Add recipe.
	 * 
	 * @param result
	 *            - result from crafting (with cost).
	 * @param recipe
	 *            - anvil recipe.
	 */
	public static void addRecipe(ItemStack result, AnvilRecipe recipe) {
		recipes.put(recipe, result);
	}

	/**
	 * Search recipe from ingredients.
	 * 
	 * @param left
	 *            - left slot item.
	 * @param right
	 *            - right slot item.
	 * @param name
	 *            - text field of anvil.
	 * @param cost
	 *            - recipe cost.
	 * @return Recipe or null.
	 */
	public static AnvilRecipe getRecipe(ItemStack left, ItemStack right, String name, int cost) {
		Iterator iterator = recipes.entrySet().iterator();
		Entry entry;
		do {
			if (!iterator.hasNext()) {
				return null;
			}
			entry = (Entry) iterator.next();
		} while (!((AnvilRecipe) entry.getKey()).equals(left, right, name, cost));
		return ((AnvilRecipe) entry.getKey());
	}

	/**
	 * Search recipe result from ingredients.
	 * 
	 * @param left
	 *            - left slot item.
	 * @param right
	 *            - right slot item.
	 * @param name
	 *            - text field of anvil.
	 * @return Result or null.
	 */
	public static ItemStack getResult(ItemStack left, ItemStack right, String name) {
		Iterator iterator = recipes.entrySet().iterator();
		Entry entry;
		do {
			if (!iterator.hasNext()) {
				return null;
			}
			entry = (Entry) iterator.next();
		} while (!((AnvilRecipe) entry.getKey()).equals(left, right, name, -1));
		return (ItemStack) entry.getValue();
	}

	/**
	 * Search recipe result from recipe.
	 * 
	 * @param recipe
	 *            - anvil recipe.
	 * @return Result or null.
	 */
	public static ItemStack getResult(AnvilRecipe recipe) {
		Iterator iterator = recipes.entrySet().iterator();
		Entry entry;
		do {
			if (!iterator.hasNext()) {
				return null;
			}
			entry = (Entry) iterator.next();
		} while (!((AnvilRecipe) entry.getKey()).equals(recipe));
		return (ItemStack) entry.getValue();
	}
}
