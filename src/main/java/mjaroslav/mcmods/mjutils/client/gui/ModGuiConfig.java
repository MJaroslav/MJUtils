package mjaroslav.mcmods.mjutils.client.gui;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import mjaroslav.mcmods.mjutils.MJInfo;
import mjaroslav.mcmods.mjutils.MJUtils;
import mjaroslav.mcmods.mjutils.common.config.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

/**
 * 
 * @author MJaroslav
 *
 */
public class ModGuiConfig extends GuiConfig {
	public ModGuiConfig(GuiScreen parentScreen) {
		super(parentScreen, getElements(), MJInfo.MODID, false, false, MJInfo.NAME);
	}

	public static List<IConfigElement> getElements() {
		ArrayList<IConfigElement> list = new ArrayList<IConfigElement>();
		List<IConfigElement> listCommon = new ConfigElement(MJUtils.config.instance.getCategory(Config.CATEGORY_COMMON))
				.getChildElements();
		List<IConfigElement> listClient = new ConfigElement(MJUtils.config.instance.getCategory(Config.CATEGORY_CLIENT))
				.getChildElements();
		list.add(new DummyCategoryElement(Config.CATEGORY_COMMON, Config.CATEGORY_COMMON, listCommon));
		list.add(new DummyCategoryElement(Config.CATEGORY_CLIENT, Config.CATEGORY_CLIENT, listClient));
		return list;
	}
}
