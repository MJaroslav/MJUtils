package mjaroslav.mcmods.mjutils.common.anvil;

import cpw.mods.fml.common.eventhandler.Event;

public class AnvilCraftingEvent extends Event {
	public AnvilRecipe recipe;

	public AnvilCraftingEvent(AnvilRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}
}
