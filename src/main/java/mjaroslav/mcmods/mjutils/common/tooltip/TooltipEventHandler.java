package mjaroslav.mcmods.mjutils.common.tooltip;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.common.config.MJUtilsConfig;
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
        boolean flag = event.showAdvancedItemTooltips && MJUtilsConfig.showOreDict;
        boolean flag1 = MJUtilsConfig.showOreDictAlways;
        if ((flag || flag1) && event.itemStack != null && event.itemStack.getItem() != null) {
            event.toolTip.add("");
            for (int id : OreDictionary.getOreIDs(event.itemStack)) {
                event.toolTip.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString()
                        + OreDictionary.getOreName(id));
            }
        }
    }
}
