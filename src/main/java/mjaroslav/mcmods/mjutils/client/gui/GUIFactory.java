package mjaroslav.mcmods.mjutils.client.gui;

import java.util.*;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import mjaroslav.mcmods.mjutils.ModMJUtils;
import mjaroslav.mcmods.mjutils.lib.ConfigInfo;
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
            super(parentScreen, getElements(), ModInfo.MODID, false, false, ModInfo.NAME);
        }

        public static List<IConfigElement> getElements() {
            ArrayList<IConfigElement> list = new ArrayList<IConfigElement>();
            List<IConfigElement> listCommon = new ConfigElement(
                    ModMJUtils.config.getInstance().getCategory(ConfigInfo.CATEGORY_COMMON)).getChildElements();
            List<IConfigElement> listClient = new ConfigElement(
                    ModMJUtils.config.getInstance().getCategory(ConfigInfo.CATEGORY_CLIENT)).getChildElements();
            list.add(new DummyCategoryElement(ConfigInfo.CATEGORY_COMMON, ConfigInfo.CATEGORY_COMMON, listCommon));
            list.add(new DummyCategoryElement(ConfigInfo.CATEGORY_CLIENT, ConfigInfo.CATEGORY_CLIENT, listClient));
            return list;
        }
    }
}
