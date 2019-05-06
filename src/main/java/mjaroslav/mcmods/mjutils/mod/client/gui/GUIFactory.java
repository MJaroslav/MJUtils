package mjaroslav.mcmods.mjutils.mod.client.gui;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import mjaroslav.mcmods.mjutils.mod.MJUtils;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

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
            super(parentScreen, MJUtils.CONFIG.generalToElementList(), ModInfo.MOD_ID, false, false, ModInfo.NAME);
        }
    }
}
