package mjaroslav.mcmods.mjutils.client.gui;

import java.util.Set;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import mjaroslav.mcmods.mjutils.ModMJUtils;
import mjaroslav.mcmods.mjutils.lib.ConfigGeneralInfo;
import mjaroslav.mcmods.mjutils.lib.ModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

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
            super(parentScreen,
                    new ConfigElement(ModMJUtils.config.getInstance().getCategory(ConfigGeneralInfo.CATEGORY))
                            .getChildElements(),
                    ModInfo.MODID, false, false, ModInfo.NAME);
        }
    }
}
