package com.github.mjaroslav.mjutils.example.client;

import com.github.mjaroslav.mjutils.example.common.modular.ExampleModule;
import com.github.mjaroslav.mjutils.example.lib.ModInfo;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

public class ExampleGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {

    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ExampleGUIConfig.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    public static class ExampleGUIConfig extends GuiConfig {
        public ExampleGUIConfig(GuiScreen parentScreen) {
            super(parentScreen, ExampleModule.config.categoryToElementList("root"), ModInfo.modId, ExampleModule.config.getFile(), false, false, ExampleModule.config.getFile(), "root");
        }
    }
}
