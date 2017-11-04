package mjaroslav.mcmods.mjutils.common.anvil;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.item.ItemStack;

/**
 * Fired when an recipe forms in the anvil.
 * 
 * @author MJaroslav
 *
 */
@Cancelable
public class AnvilCraftingEvent extends Event {
	/**
	 * Current anvil recipe.
	 */
	public AnvilRecipe recipe;

	/**
	 * Result of crafting. Can be changed.
	 */
	public ItemStack result;

	/**
	 * @see AnvilCraftingEvent
	 * 
	 * @param recipe
	 *            - current recipe.
	 * @param result
	 *            - result from crafting. Can be changed.
	 */
	public AnvilCraftingEvent(AnvilRecipe recipe, ItemStack result) {
		this.recipe = recipe.copy();
		this.result = result.copy();
	}

	@Override
	public boolean isCancelable() {
		return true;
	}
}
