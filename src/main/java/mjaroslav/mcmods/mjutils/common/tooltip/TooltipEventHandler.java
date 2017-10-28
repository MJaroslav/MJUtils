package mjaroslav.mcmods.mjutils.common.tooltip;

import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.common.config.Config;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * 
 * @author MJaroslav
 *
 */
public class TooltipEventHandler {
	@SubscribeEvent
	public void itemTooltipEvent(ItemTooltipEvent event) {
		boolean flag = event.showAdvancedItemTooltips && Config.showOreDict;
		boolean flag1 = Config.showOreDictAllways;
		if ((flag || flag1) && event.itemStack != null && event.itemStack.getItem() != null) {
			boolean flag2 = false;
			for (int id : OreDictionary.getOreIDs(event.itemStack)) {
				if (!flag2) {
					event.toolTip.add("");
					flag2 = true;
				}
				event.toolTip.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString()
						+ OreDictionary.getOreName(id));
			}
		}
	}
}
