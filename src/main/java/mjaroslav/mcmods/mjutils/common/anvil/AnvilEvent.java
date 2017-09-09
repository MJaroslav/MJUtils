package mjaroslav.mcmods.mjutils.common.anvil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;

public class AnvilEvent {
	@SubscribeEvent
	public void onAnvilUpdateEvent(AnvilUpdateEvent event) {
		ItemStack result = AnvilUtils.instance().getResult(event.left, event.right, event.name);
		if (result != null) {
			result.stackSize = event.left.stackSize;
			if (result.stackSize > result.getMaxStackSize())
				result.stackSize = result.getMaxStackSize();
			event.output = result;
			event.cost = AnvilUtils.instance().getLevels(event.left, event.right, event.name) * result.stackSize;
		}
	}
}
