package com.github.mjaroslav.mjutils.hook;

import com.github.mjaroslav.mjutils.configurator.HooksConfigurator;
import gloomyfolken.hooklib.minecraft.HookLoader;
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;

// TODO: Don't forget -Dfml.coreMods.load=com.github.mjaroslav.mjutils.hook.MJUtilsHookLoader
@MCVersion("1.7.10")
@Name(ModInfo.modId)
public class MJUtilsHookLoader extends HookLoader {
    public static final HooksConfigurator config = new HooksConfigurator(ModInfo.modId, ResourcePath.of("mjutils:configurators/hooks.properties"));

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    @Override
    protected void registerHooks() {
        config.load();
        ModInfo.loggerHooks.debug("Enabled hooks: " + String.join(", ", config.getEnabledHooks()));
        for (String hook : config.getEnabledHooks())
            registerHookContainer(hook);
    }
}
