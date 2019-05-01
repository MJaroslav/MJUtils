package mjaroslav.mcmods.mjutils.mod.common.handler;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.mod.lib.CategoryGeneral.CategoryClient;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class TooltipEventHandler {
    public static final TooltipEventHandler instance = new TooltipEventHandler();

    private TooltipEventHandler() {
    }

    @SubscribeEvent
    public void itemTooltipEvent(ItemTooltipEvent event) {
        if (((event.showAdvancedItemTooltips && CategoryClient.showOreDictNames) ||
                CategoryClient.alwaysShowOreDictNames) && event.itemStack != null && event.itemStack.getItem() != null
                && OreDictionary.getOreIDs(event.itemStack).length > 0) {
            List<String> lines = new ArrayList<>();
            StringBuilder line = new StringBuilder();
            String ore;
            for (int id : OreDictionary.getOreIDs(event.itemStack)) {
                ore = OreDictionary.getOreName(id);
                if ((line + ore).length() < 40)
                    line.append(" ").append(ore);
                else if (line.length() == 0)
                    lines.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString() + ore);
                else {
                    lines.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString() +
                            line.toString().trim());
                    line = new StringBuilder();
                }
            }
            if (lines.size() > 0) {
                event.toolTip.add("");
                event.toolTip.addAll(lines);
            }
        }
    }
}
