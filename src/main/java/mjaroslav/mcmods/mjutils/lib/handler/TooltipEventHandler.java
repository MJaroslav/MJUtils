package mjaroslav.mcmods.mjutils.lib.handler;

import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.mod.lib.ConfigClientInfo;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Event handler for tooltip utils.
 *
 * @author MJaroslav
 */
public class TooltipEventHandler {
    @SubscribeEvent
    public void itemTooltipEvent(ItemTooltipEvent event) {
        boolean flag = event.showAdvancedItemTooltips && ConfigClientInfo.showOreDict;
        boolean flag1 = ConfigClientInfo.showOreDictAlways;
        if ((flag || flag1) && event.itemStack != null && event.itemStack.getItem() != null
                && OreDictionary.getOreIDs(event.itemStack).length > 0) {
            event.toolTip.add("");
            for (int id : OreDictionary.getOreIDs(event.itemStack)) {
                event.toolTip.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString()
                        + OreDictionary.getOreName(id));
            }
        }
    }
}
