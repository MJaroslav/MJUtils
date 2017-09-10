package mjaroslav.mcmods.mjutils.common.anvil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;

public class AnvilEvent {
	@SubscribeEvent
	public void onAnvilUpdateEvent(AnvilUpdateEvent event) {
		ItemStack result = AnvilUtils.instance().getResult(event.left, event.right, event.name);
		if (result != null) {
			ItemStack output = result.copy();
			output.stackSize = output.stackSize * event.left.stackSize;
			if (output.stackSize <= output.getMaxStackSize()) {
				event.output = output;
				event.materialCost = AnvilUtils.instance().getRightCount(event.left, event.right, event.name)
						* event.left.stackSize;
				int levels = AnvilUtils.instance().getLevels(event.left, event.right, event.name);
				event.cost = levels + (event.left.stackSize > 1 ? (int) (levels * event.left.stackSize / 2) : 0);
			}
		}
	}
}
