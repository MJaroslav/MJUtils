package mjaroslav.mcmods.mjutils.common.tooltip;

import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.lib.ConfigInfo;
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
        boolean flag = event.showAdvancedItemTooltips && ConfigInfo.showOreDict;
        boolean flag1 = ConfigInfo.showOreDictAlways;
        if ((flag || flag1) && event.itemStack != null && event.itemStack.getItem() != null) {
            event.toolTip.add("");
            for (int id : OreDictionary.getOreIDs(event.itemStack)) {
                event.toolTip.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString()
                        + OreDictionary.getOreName(id));
            }
        }
    }
}
