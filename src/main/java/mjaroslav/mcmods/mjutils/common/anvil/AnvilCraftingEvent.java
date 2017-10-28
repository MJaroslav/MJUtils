package mjaroslav.mcmods.mjutils.common.anvil;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Fired when an recipe forms in the anvil.
 * 
 * @author MJaroslav
 *
 */
public class AnvilCraftingEvent extends Event {
	/**
	 * Current recipe of anvil craft.
	 */
	public AnvilRecipe recipe;

	public AnvilCraftingEvent(AnvilRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}
}
