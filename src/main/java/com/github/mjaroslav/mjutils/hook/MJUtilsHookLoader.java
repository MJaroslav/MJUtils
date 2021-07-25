package com.github.mjaroslav.mjutils.hook;

import com.github.mjaroslav.mjutils.configurator.impl.loader.HookConfiguratorLoader;
import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.minecraft.HookLoader;
import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;

// TODO: Don't forget -Dfml.coreMods.load=com.github.mjaroslav.mjutils.hook.MJUtilsHookLoader
@MCVersion("1.7.10")
@Name(ModInfo.MOD_ID)
public class MJUtilsHookLoader extends HookLoader {
    public static final HookConfiguratorLoader CONFIG = new HookConfiguratorLoader(ModInfo.MOD_ID, String.format("%s_hooks", ModInfo.MOD_ID), ResourcePath.of("assets/mjutils/configurators/hooks.properties"));

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    @Override
    protected void registerHooks() {
        CONFIG.load();
        for (String hook : CONFIG.getEnabledHooks())
            registerHookContainer(hook);
//        if (HookConfig.blockBreakingCreative())
//            registerHookContainer(HooksBlockBreakingCreative.class.getName());
//        else ModInfo.LOG.warn(String.format("Hook \"%s\" disabled! All dependent methods will be ignored!",
//                HooksBlockBreakingCreative.DISABLE_ID));
//        if (HookConfig.fishingEvent())
//            registerHookContainer(HooksFishingEvent.class.getName());
//        else
//            ModInfo.LOG.warn(String.format("Hook \"%s\" disabled! All dependent methods will be ignored!",
//                    HooksFishingEvent.DISABLE_ID));
//        if (HookConfig.fishingCache())
//            registerHookContainer(HooksFishingCache.class.getName());
//        else
//            ModInfo.LOG.warn(String.format("Hook \"%s\" disabled! All dependent methods will be use original!",
//                    HooksFishingEvent.DISABLE_ID));
//        if (HookConfig.fishingNullFix())
//            registerHookContainer(HooksFishingNullFix.class.getName());
//        else ModInfo.LOG.warn(String.format("Hook \"%s\" disabled! You may catch crashes while you will use " +
//                        "clear(-All) in UtilsFishing!",
//                HooksFishingEvent.DISABLE_ID));
//        if (HookConfig.fishingNullFix() || HookConfig.fishingEvent())
//            registerHookContainer(HooksGetRandomFishable.class.getName());
    }
}
