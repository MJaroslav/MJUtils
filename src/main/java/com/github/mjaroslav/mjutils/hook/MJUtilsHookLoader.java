package com.github.mjaroslav.mjutils.hook;

import com.github.mjaroslav.mjutils.configurator.HooksConfigurator;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
//import com.github.mjaroslav.mjutils.util.logging.ModLogger;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
//import gloomyfolken.hooklib.asm.HookLogger;
//import gloomyfolken.hooklib.asm.HookLoggerManager;
//import gloomyfolken.hooklib.minecraft.HookLoader;
//import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;

// TODO: Don't forget -Dfml.coreMods.load=com.github.mjaroslav.mjutils.hook.MJUtilsHookLoader
@MCVersion("1.7.10")
@Name(ModInfo.modId)
public class MJUtilsHookLoader /*extends HookLoader*/ {
    static {
        //HookLoggerManager.setLogger(new Log4j2HookLogger());
    }

    public static final HooksConfigurator config = new HooksConfigurator(ModInfo.modId, ResourcePath.of("mjutils:configurators/hooks.properties"));

    //@Override
    public String[] getASMTransformerClass() {
        return new String[]{/*PrimaryClassTransformer.class.getName()*/};
    }

    //@Override
    protected void registerHooks() {
        config.load();
        ModInfo.loggerHooks.debug("Enabled hooks: " + String.join(", ", config.getEnabledHooks()));
        for (String hook : config.getEnabledHooks())
            ;//registerHookContainer(hook);
    }

    /*static class Log4j2HookLogger implements HookLogger {
        final ModLogger logger = ModInfo.loggerHooks;

        @Override
        public void debug(String s) {
            logger.debug(s);
        }

        @Override
        public void warning(String s) {
            logger.warn(s);
        }

        @Override
        public void severe(String s) {
            logger.error(s);
        }

        @Override
        public void severe(String s, Throwable throwable) {
            logger.error(s, throwable);
        }
    }*/
}
