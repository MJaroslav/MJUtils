package mjaroslav.mcmods.mjutils.common.anvil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

public class AnvilUtils {
	private Map<AnvilRecipe, AnvilResult> recipes = new HashMap();

	private static final AnvilUtils instance = new AnvilUtils();

	public static AnvilUtils instance() {
		return instance;
	}

	public Map<AnvilRecipe, AnvilResult> getRecipes() {
		return recipes;
	}

	public void addRecipe(ItemStack result, AnvilRecipe recipe) {
		this.addRecipe(result, 0, recipe);
	}

	public void addRecipe(ItemStack result, int levels, AnvilRecipe recipe) {
		this.addRecipe(new AnvilResult(result, levels), recipe);
	}

	public void addRecipe(AnvilResult result, AnvilRecipe recipe) {
		this.recipes.put(recipe, result);
	}

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

	public int getLevels(ItemStack left, ItemStack right, String name) {
		Iterator iterator = this.recipes.entrySet().iterator();
		Entry entry;
		do {
			if (!iterator.hasNext()) {
				return 0;
			}
			entry = (Entry) iterator.next();
		} while (!((AnvilRecipe) entry.getKey()).equals(left, right, name));
		return ((AnvilResult) entry.getValue()).levels;
	}

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

	public static class AnvilResult {
		public int levels;
		public ItemStack result;

		public AnvilResult(ItemStack result, int levels) {
			this.result = result;
			this.levels = levels;
		}
	}
}
