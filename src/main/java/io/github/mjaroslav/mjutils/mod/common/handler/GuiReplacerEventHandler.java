package io.github.mjaroslav.mjutils.mod.common.handler;

import io.github.mjaroslav.mjutils.mod.client.gui.replace.modlist.GuiModListReplacer;
import io.github.mjaroslav.mjutils.mod.lib.General.Client.GuiReplacements;
import cpw.mods.fml.client.GuiIngameModOptions;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GuiReplacerEventHandler {
    public static final GuiReplacerEventHandler instance = new GuiReplacerEventHandler();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiModList &&
                GuiReplacements.mainMenuModList ||
                event.gui instanceof GuiIngameModOptions && GuiReplacements.optionsModList)
            event.gui = new GuiModListReplacer(Minecraft.getMinecraft().currentScreen);
    }
}
