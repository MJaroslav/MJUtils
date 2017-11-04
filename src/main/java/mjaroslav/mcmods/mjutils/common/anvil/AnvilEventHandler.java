package mjaroslav.mcmods.mjutils.common.anvil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;

public class AnvilEventHandler {
	@SubscribeEvent
	public void onAnvilUpdateEvent(AnvilUpdateEvent event) {
		AnvilRecipe recipe = AnvilUtils.getRecipe(event.left, event.right, event.name, -1);
		if (recipe != null) {
			ItemStack result = AnvilUtils.getResult(recipe);
			AnvilCraftingEvent newEvent = new AnvilCraftingEvent(recipe, result);
			if (MinecraftForge.EVENT_BUS.post(newEvent))
				return;
			if (newEvent.result.getMaxDamage() != 1 && event.left.stackSize * newEvent.recipe.rightStack.stackSize
					* newEvent.result.stackSize > newEvent.result.getMaxStackSize())
				return;
			if (event.left.stackSize * newEvent.recipe.rightStack.stackSize > newEvent.recipe.rightStack
					.getMaxStackSize())
				return;
			newEvent.result.stackSize = event.left.stackSize * newEvent.recipe.rightStack.stackSize
					* newEvent.result.stackSize;
			int cost = (event.left.stackSize > 1)
					? (newEvent.recipe.cost + (int) (newEvent.recipe.cost * event.left.stackSize / 2))
					: newEvent.recipe.cost;
			if (cost < 1)
				cost = 1;
			int materialCost = newEvent.recipe.rightStack.stackSize * newEvent.recipe.leftStack.stackSize;
			event.output = newEvent.result;
			event.materialCost = materialCost * event.left.stackSize;
			event.cost = cost;
		}

	}
}
