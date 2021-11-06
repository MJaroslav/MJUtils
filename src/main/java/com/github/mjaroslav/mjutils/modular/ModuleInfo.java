package com.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLStateEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ModuleInfo {
    @Nonnull
    public final String[] modDependencies;
    public final int priority;
    @Nonnull
    public final ModState loadOn;
    @Nonnull
    public final String moduleClassName;
    @Getter
    private Modular module;

    private boolean loaded;

    ModuleInfo(@Nonnull Proxy proxy) {
        // Placeholders
        modDependencies = new String[0]; // Just placeholder for Nonnull, proxy must have no dependencies
        priority = Integer.MAX_VALUE; // Proxy must be last in load queue
        loadOn = ModState.CONSTRUCTED; // It's real hardcoded state for proxy
        moduleClassName = proxy.getClass().getName(); // Just placeholder for Nonnull, module loading never been called in ths case

        module = proxy;
        loaded = true;
    }

    public boolean isAllRequiredModsLoaded() {
        for (String modid : modDependencies)
            if (!Loader.isModLoaded(modid))
                return false;
        return true;
    }

    private boolean canListen(@Nonnull FMLEvent event) {
        if (!loaded) {
            if ((event instanceof FMLStateEvent) && ((FMLStateEvent) event).getModState() == loadOn
                    || event instanceof FMLInterModComms.IMCEvent) {
                try {
                    Object instance = Class.forName(moduleClassName).newInstance();
                    if (instance instanceof Modular) {
                        module = (Modular) instance;
                        loaded = true;
                    }
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return loaded;
    }

    public void listen(@Nonnull FMLEvent event) {
        if (canListen(event))
            try {
                module.getClass().getMethod("listen", event.getClass()).invoke(module, event);
            } catch (NoSuchMethodException ignored) {
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
    }
}
