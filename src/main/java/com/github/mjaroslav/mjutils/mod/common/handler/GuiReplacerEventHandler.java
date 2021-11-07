package com.github.mjaroslav.mjutils.mod.common.handler;

import com.github.mjaroslav.mjutils.mod.client.gui.GuiModListReplacer;
import com.github.mjaroslav.mjutils.mod.lib.CategoryRoot;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GuiReplacerEventHandler {
    public static final GuiReplacerEventHandler instance = new GuiReplacerEventHandler();

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiModList &&
                CategoryRoot.CategoryClient.CategoryGuiReplacements.mainMenuModList)
            event.gui = new GuiModListReplacer(Minecraft.getMinecraft().currentScreen);
    }
}
