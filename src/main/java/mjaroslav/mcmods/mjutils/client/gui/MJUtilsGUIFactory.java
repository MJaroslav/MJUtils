package mjaroslav.mcmods.mjutils.client.gui;

import java.util.*;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import mjaroslav.mcmods.mjutils.MJInfo;
import mjaroslav.mcmods.mjutils.MJUtils;
import mjaroslav.mcmods.mjutils.common.config.MJUtilsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public class MJUtilsGUIFactory implements IModGuiFactory {
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
            super(parentScreen, getElements(), MJInfo.MODID, false, false, MJInfo.NAME);
        }

        public static List<IConfigElement> getElements() {
            ArrayList<IConfigElement> list = new ArrayList<IConfigElement>();
            List<IConfigElement> listCommon = new ConfigElement(
                    MJUtils.config.getInstance().getCategory(MJUtilsConfig.CATEGORY_COMMON)).getChildElements();
            List<IConfigElement> listClient = new ConfigElement(
                    MJUtils.config.getInstance().getCategory(MJUtilsConfig.CATEGORY_CLIENT)).getChildElements();
            list.add(
                    new DummyCategoryElement(MJUtilsConfig.CATEGORY_COMMON, MJUtilsConfig.CATEGORY_COMMON, listCommon));
            list.add(
                    new DummyCategoryElement(MJUtilsConfig.CATEGORY_CLIENT, MJUtilsConfig.CATEGORY_CLIENT, listClient));
            return list;
        }
    }
}
