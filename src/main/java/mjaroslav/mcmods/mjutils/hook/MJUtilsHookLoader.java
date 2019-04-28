package mjaroslav.mcmods.mjutils.hook;

import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.minecraft.HookLoader;
import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;

// Don't forget -Dfml.coreMods.load=mjaroslav.mcmods.mjutils.hook.MJUtilsHookLoader
@SuppressWarnings("unused")
public class MJUtilsHookLoader extends HookLoader {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    @Override
    protected void registerHooks() {
        if (HookConfig.blockBreakingCreative())
            registerHookContainer(HooksBlockBreakingCreative.class.getName());
        else ModInfo.LOG.warn(String.format("Hook \"%s\" disabled! All dependent methods will be ignored!",
                HooksBlockBreakingCreative.DISABLE_ID));
        if (HookConfig.fishingEvent())
            registerHookContainer(HooksFishingEvent.class.getName());
        else
            ModInfo.LOG.warn(String.format("Hook \"%s\" disabled! All dependent methods will be ignored!",
                    HooksFishingEvent.DISABLE_ID));
        if (HookConfig.fishingCache())
            registerHookContainer(HooksFishingCache.class.getName());
        else
            ModInfo.LOG.warn(String.format("Hook \"%s\" disabled! All dependent methods will be use original!",
                    HooksFishingEvent.DISABLE_ID));
    }
}
