package io.github.mjaroslav.mjutils.internal.client.listener;

import cpw.mods.fml.client.GuiIngameModOptions;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mjaroslav.mjutils.internal.client.gui.replace.modlist.GuiModListReplacer;
import io.github.mjaroslav.mjutils.lib.General.Client.GuiReplacements;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GuiReplacerEventListener {
    public static final GuiReplacerEventListener INSTANCE = new GuiReplacerEventListener();

    @SubscribeEvent
    public void onGuiOpenEvent(@NotNull GuiOpenEvent event) {
        if (event.gui instanceof GuiModList && GuiReplacements.mainMenuModList ||
            event.gui instanceof GuiIngameModOptions && GuiReplacements.optionsModList)
            event.gui = new GuiModListReplacer(Minecraft.getMinecraft().currentScreen);
    }
}
