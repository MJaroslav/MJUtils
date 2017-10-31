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
	private Map<AnvilRecipe, AnvilResult> recipes = new HashMap();

	/**
	 * Instance of this handler, use {@link AnvilUtils#instance()}.
	 */
	private static final AnvilUtils instance = new AnvilUtils();

	/**
	 * Get instance of this handler.
	 * 
	 * @return Instance of this handler. All recipes are added through it.
	 */
	public static AnvilUtils instance() {
		return instance;
	}

	/**
	 * Get anvil recipe map.
	 * 
	 * @return Recipe - result map.
	 */
	public Map<AnvilRecipe, AnvilResult> getRecipes() {
		return recipes;
	}

	/**
	 * Add recipe, default cost - 1Lvl.
	 * 
	 * @param result
	 *            - result from crafting.
	 * @param recipe
	 *            - anvil recipe.
	 */
	public void addRecipe(ItemStack result, AnvilRecipe recipe) {
		this.addRecipe(result, 1, recipe);
	}

	/**
	 * Add recipe.
	 * 
	 * @param result
	 *            - result from crafting.
	 * @param levels
	 *            - cost, [1;+).
	 * @param recipe
	 *            - anvil recipe.
	 */
	public void addRecipe(ItemStack result, int levels, AnvilRecipe recipe) {
		if (levels < 1)
			levels = 1;
		this.addRecipe(new AnvilResult(result, levels), recipe);
	}

	/**
	 * Add recipe.
	 * 
	 * @param result
	 *            - result from crafting (with cost).
	 * @param recipe
	 *            - anvil recipe.
	 */
	public void addRecipe(AnvilResult result, AnvilRecipe recipe) {
		this.recipes.put(recipe, result);
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
	public ItemStack getResult(ItemStack left, ItemStack right, String name) {
		Iterator iterator = this.recipes.entrySet().iterator();
		Entry entry;
		do {
			if (!iterator.hasNext()) {
				return null;
			}
			entry = (Entry) iterator.next();
		} while (!((AnvilRecipe) entry.getKey()).equals(left, right, name));
		return ((AnvilResult) entry.getValue()).result;
	}

	/**
	 * Search recipe cost from ingredients.
	 * 
	 * @param left
	 *            - left slot item.
	 * @param right
	 *            - right slot item.
	 * @param name
	 *            - text field of anvil.
	 * @return Cost [1;+).
	 */
	public int getLevels(ItemStack left, ItemStack right, String name) {
		Iterator iterator = this.recipes.entrySet().iterator();
		Entry entry;
		do {
			if (!iterator.hasNext()) {
				return 1;
			}
			entry = (Entry) iterator.next();
		} while (!((AnvilRecipe) entry.getKey()).equals(left, right, name));
		int levels = ((AnvilResult) entry.getValue()).levels;
		if (levels < 1)
			levels = 1;
		return 1;
	}

	/**
	 * Search recipe right item stackSize from ingredients.
	 * 
	 * @param left
	 *            - left slot item.
	 * @param right
	 *            - right slot item.
	 * @param name
	 *            - text field of anvil.
	 * @return StackSize of right item or 0.
	 */
	public int getRightCount(ItemStack left, ItemStack right, String name) {
		Iterator iterator = this.recipes.entrySet().iterator();
		Entry entry;
		do {
			if (!iterator.hasNext()) {
				return 0;
			}
			entry = (Entry) iterator.next();
		} while (!((AnvilRecipe) entry.getKey()).equals(left, right, name));
		return ((AnvilRecipe) entry.getKey()).getRight().stackSize;
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
	 * @return Recipe or null.
	 */
	public AnvilRecipe getRecipe(ItemStack left, ItemStack right, String name) {
		Iterator iterator = this.recipes.entrySet().iterator();
		Entry entry;
		do {
			if (!iterator.hasNext()) {
				return null;
			}
			entry = (Entry) iterator.next();
		} while (!((AnvilRecipe) entry.getKey()).equals(left, right, name));
		return ((AnvilRecipe) entry.getKey());
	}

	/**
	 * Result of recipe on anvil.
	 * 
	 * @author MJaroslav
	 *
	 */
	public static class AnvilResult {
		/**
		 * Cost.
		 */
		public int levels;
		/**
		 * Result item.
		 */
		public ItemStack result;

		/**
		 * Anvil result blank.
		 * 
		 * @param result
		 *            - result item.
		 * @param levels
		 *            - cost [1;+) Lvls.
		 */
		public AnvilResult(ItemStack result, int levels) {
			if (levels < 1)
				levels = 1;
			this.result = result;
			this.levels = levels;
		}
	}
}
