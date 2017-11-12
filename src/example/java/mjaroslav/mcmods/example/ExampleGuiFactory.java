package mjaroslav.mcmods.example;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import java.util.Set;

public class ExampleGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return ExampleGuiScreen.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

    public static class ExampleGuiScreen extends GuiConfig {
        public ExampleGuiScreen(GuiScreen parentScreen) {
            super(parentScreen,
                    new ConfigElement(ExampleMod.config.getInstance().getCategory(ExampleConfig.categoryExample))
                            .getChildElements(),
                    ExampleInfo.MODID, false, false, "config/" + ExampleInfo.MODID + ".cfg");
            // You should only use an instance of your configuration. In this
            // example, it is in the main class.
        }

    }
}
