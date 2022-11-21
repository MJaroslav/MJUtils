package io.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLStateEvent;
import io.github.mjaroslav.mjutils.asm.mixin.AccessorFMLModContainer;
import io.github.mjaroslav.mjutils.internal.lib.ModInfo;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ModuleInfo {
    private final @NotNull String[] modDependencies;
    private final int priority;
    private final @NotNull ModState loadOn;
    private final @NotNull String moduleClassName;
    private Object module;
    private boolean loaded; // Marker for single loading
    private boolean error; // Marker for prevent many log messages with loading error

    ModuleInfo(@NotNull Proxy proxy) {
        // Placeholders
        modDependencies = new String[0]; // Just placeholder for Nonnull, proxy must have no dependencies
        priority = Integer.MAX_VALUE; // Proxy must be last in load queue
        loadOn = ModState.CONSTRUCTED; // It's real hardcoded state for proxy
        moduleClassName = proxy.getClass().getName(); // Just placeholder for Nonnull, module loading never been called in ths case
        module = proxy;
        loaded = true;
    }

    public boolean isAllRequiredModsLoaded() {
        return UtilsMods.isModsLoaded(modDependencies);
    }

    private boolean canListen(@NotNull FMLEvent event) {
        if (!loaded && !error)
            if (event instanceof FMLStateEvent stateEvent && stateEvent.getModState() == loadOn
                || event instanceof FMLInterModComms.IMCEvent)
                try {
                    module = Class.forName(moduleClassName).getConstructor().newInstance();
                    loaded = true;
                } catch (Exception e) {
                    ModInfo.loggerModules.error("Can't load \"%s\" module", e, moduleClassName);
                    error = true;
                }
        return loaded;
    }

    public void listen(@NotNull FMLModContainer container, @NotNull FMLEvent event) {
        if (canListen(event))
            try {
                module.getClass().getMethod("listen", event.getClass()).invoke(module, event);
            } catch (NoSuchMethodException ignored) {
                ModInfo.loggerModules.debug("Listener for %s in %s not found", event.getEventType(), container);
            } catch (InvocationTargetException e) {
                ((AccessorFMLModContainer) container).getController().errorOccurred(container, e.getCause());
            } catch (IllegalAccessException e) {
                ModInfo.loggerModules.warn("Can't load event %s from %s module, listeners should be non-static public",
                    moduleClassName);
            }
    }
}
