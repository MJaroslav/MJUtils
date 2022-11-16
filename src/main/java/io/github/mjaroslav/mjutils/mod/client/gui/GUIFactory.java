package io.github.mjaroslav.mjutils.mod.client.gui;

import io.github.mjaroslav.mjutils.mod.common.modular.MainModule;
import io.github.mjaroslav.mjutils.mod.lib.ModInfo;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;

import java.util.Set;

public class GUIFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return MJUtilsGUIConfig.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    public static class MJUtilsGUIConfig extends GuiConfig {
        public MJUtilsGUIConfig(GuiScreen parentScreen) {
            super(parentScreen, MainModule.config.getElementList(), ModInfo.modId, MainModule.config.getConfigId(),
                    false, false, MainModule.config.getFile().toString(), Configuration.CATEGORY_GENERAL);
        }
    }
}
