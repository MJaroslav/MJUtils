package mjaroslav.mcmods.mjutils.mod.common.handler;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.mod.lib.ConfigGeneralInfo;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

public class TooltipEventHandler {
    public static final TooltipEventHandler instance = new TooltipEventHandler();

    private TooltipEventHandler() {
    }

    @SubscribeEvent
    public void itemTooltipEvent(ItemTooltipEvent event) {
        if (((event.showAdvancedItemTooltips && ConfigGeneralInfo.showOreDict) || ConfigGeneralInfo.showOreDictAlways)
                && event.itemStack != null && event.itemStack.getItem() != null
                && OreDictionary.getOreIDs(event.itemStack).length > 0) {
            event.toolTip.add("");
            for (int id : OreDictionary.getOreIDs(event.itemStack))
                event.toolTip.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString()
                        + OreDictionary.getOreName(id));
        }
    }
}
