package io.github.mjaroslav.mjutils.internal.common.listener;

import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.mjaroslav.mjutils.item.Stacks;
import io.github.mjaroslav.mjutils.lib.General.Client;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TooltipEventListener {
    public static final TooltipEventListener instance = new TooltipEventListener();

    // TODO: Remake this
    @SubscribeEvent
    public void itemTooltipEvent(ItemTooltipEvent event) {
        if (((event.showAdvancedItemTooltips && Client.showOreDictNames) ||
            Client.alwaysShowOreDictNames) && Stacks.isNotEmpty(event.itemStack)
            && OreDictionary.getOreIDs(event.itemStack).length > 0) {
            List<String> lines = new ArrayList<>();
            StringBuilder line = new StringBuilder();
            String ore;
            for (int id : OreDictionary.getOreIDs(event.itemStack)) {
                ore = OreDictionary.getOreName(id);
                if ((line.toString() + ore).length() < 40)
                    line.append(" ").append(ore);
                else if (line.length() == 0)
                    lines.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString() + ore);
                else {
                    lines.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString() +
                        line.toString().trim());
                    line = new StringBuilder();
                }
            }
            if (line.length() > 0)
                lines.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString() +
                    line.toString().trim());
            if (lines.size() > 0) {
                event.toolTip.add("");
                event.toolTip.add(ChatFormatting.DARK_GRAY.toString() + ChatFormatting.ITALIC.toString() +
                    "OreDictionary names:");
                event.toolTip.addAll(lines);
            }
        }
    }
}
