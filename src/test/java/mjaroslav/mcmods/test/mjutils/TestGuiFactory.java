package mjaroslav.mcmods.test.mjutils;

import java.util.Set;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public class TestGuiFactory implements IModGuiFactory {
	public static class TestGuiScreen extends GuiConfig {

		public TestGuiScreen(GuiScreen parentScreen) {
			super(parentScreen, new ConfigElement(MJUtilsTest.config.getInstance().getCategory("gig")).getChildElements(),
					"mjutilstest", false, false, "MJUtils");
		}

	}

	@Override
	public void initialize(Minecraft minecraftInstance) {
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return TestGuiScreen.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}
}
