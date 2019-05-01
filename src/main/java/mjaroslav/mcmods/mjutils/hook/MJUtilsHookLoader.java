package mjaroslav.mcmods.mjutils.hook;

import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.minecraft.HookLoader;
import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;

// TODO: Don't forget -Dfml.coreMods.load=mjaroslav.mcmods.mjutils.hook.MJUtilsHookLoader
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
        if (HookConfig.fishingNullFix())
            registerHookContainer(HooksFishingNullFix.class.getName());
        else ModInfo.LOG.warn(String.format("Hook \"%s\" disabled! You may catch crashes while you will use " +
                        "clear(-All) in UtilsFishing!",
                HooksFishingEvent.DISABLE_ID));
        if (HookConfig.fishingNullFix() || HookConfig.fishingEvent())
            registerHookContainer(HookGetRandomFishable.class.getName());
    }
}
