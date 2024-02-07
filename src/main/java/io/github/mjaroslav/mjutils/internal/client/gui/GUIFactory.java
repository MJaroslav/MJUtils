package io.github.mjaroslav.mjutils.internal.client.gui;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import io.github.mjaroslav.mjutils.asm.MJUtilsPlugin;
import io.github.mjaroslav.mjutils.asm.MixinPatches;
import io.github.mjaroslav.mjutils.internal.common.modular.MainModule;
import io.github.mjaroslav.mjutils.lib.ModInfo;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Arrays;
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
            super(parentScreen, Arrays.asList(MJUtilsPlugin.CONFIG.getCategoryElement(MixinPatches.CATEGORY_MIXINS),
                    MainModule.CONFIG.getCategoryElement()), ModInfo.MOD_ID, ModInfo.MOD_ID + "@", false, false,
                MJUtilsGUIConfig.getAbridgedConfigPath(UtilsMods.getMinecraftDir().toPath().resolve("config")
                    .resolve(ModInfo.MOD_ID).toAbsolutePath().normalize() + "/"));
        }
    }
}
