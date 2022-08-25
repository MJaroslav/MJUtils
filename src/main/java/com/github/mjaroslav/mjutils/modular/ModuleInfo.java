package com.github.mjaroslav.mjutils.modular;

import com.github.mjaroslav.mjutils.asm.mixin.AccessorFMLModContainer;
import com.github.mjaroslav.mjutils.util.game.UtilsMods;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLStateEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class ModuleInfo {
    final @NotNull String[] modDependencies;
    final int priority;
    final @NotNull ModState loadOn;
    final @NotNull String moduleClassName;
    @Getter
    private Object module;

    private boolean loaded; // Marker for single loading
    private boolean errored; // Marker for prevent many log messages with loading error

    ModuleInfo(@NotNull Proxy proxy) {
        // Placeholders
        modDependencies = new String[0]; // Just placeholder for Nonnull, proxy must have no dependencies
        priority = Integer.MAX_VALUE; // Proxy must be last in load queue
        loadOn = ModState.CONSTRUCTED; // It's real hardcoded state for proxy
        moduleClassName = proxy.getClass().getName(); // Just placeholder for Nonnull, module loading never been called in ths case

        module = proxy;
        loaded = true;
    }

    boolean isAllRequiredModsLoaded() {
        return UtilsMods.isModsLoaded(modDependencies);
    }

    private boolean canListen(@NotNull FMLEvent event) {
        if (!loaded && !errored) {
            if ((event instanceof FMLStateEvent) && ((FMLStateEvent) event).getModState() == loadOn
                    || event instanceof FMLInterModComms.IMCEvent) {
                try {
                    module = Class.forName(moduleClassName).newInstance();
                    loaded = true;
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    ModuleLoader.log.error("Can't load \"%s\" module", e, moduleClassName);
                    errored = true;
                }
            }
        }
        return loaded;
    }

    void listen(@NotNull FMLModContainer container, @NotNull FMLEvent event) {
        if (canListen(event))
            try {
                module.getClass().getMethod("listen", event.getClass()).invoke(module, event);
            } catch (NoSuchMethodException ignored) {
            } catch (InvocationTargetException e) {
                ((AccessorFMLModContainer) container).getController().errorOccurred(container, e.getCause());
            } catch (IllegalAccessException e) {
                ModuleLoader.log.warn("Can't load event %s from %s module, listeners should be non-static public",
                        moduleClassName);
            }
    }
}
