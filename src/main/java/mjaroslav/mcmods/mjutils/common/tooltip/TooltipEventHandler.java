package mjaroslav.mcmods.mjutils.common.tooltip;

import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

public class TooltipEventHandler {
	@SubscribeEvent
	public void itemTooltipEvent(ItemTooltipEvent event) {
		if (event.showAdvancedItemTooltips && event.itemStack != null && event.itemStack.getItem() != null) {
			boolean flag = false;
			for (int id : OreDictionary.getOreIDs(event.itemStack)) {
				if (!flag) {
					event.toolTip.add("");
					flag = true;
				}
				event.toolTip.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString()
						+ OreDictionary.getOreName(id));
			}
		}
	}
}
