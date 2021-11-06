package com.github.mjaroslav.mjutils.hook;

import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm.Hook;
import com.github.mjaroslav.mjutils.modular.Loader;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLEvent;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class HooksFMLModContainer {
    private static final Map<FMLModContainer, ModuleLoader> loaders = new HashMap<>();

    @Nullable
    public static ModuleLoader getActiveLoader() {
        ModContainer mc = cpw.mods.fml.common.Loader.instance().activeModContainer();
        return mc instanceof FMLModContainer ? getOrTryCreateLoader((FMLModContainer) mc) : null;
    }

    @Nullable
    private static ModuleLoader getOrTryCreateLoader(@Nonnull FMLModContainer container) {
        if (loaders.containsKey(container))
            return loaders.get(container);
        else {
            Object modInstance = container.getMod();
            for (Field field : modInstance.getClass().getFields())
                if (field.isAnnotationPresent(Loader.class)) {
                    String packageName = modInstance.getClass().getName();
                    packageName = packageName.substring(0, packageName.lastIndexOf("."));
                    ModuleLoader loader = new ModuleLoader(container.getModId(), modInstance,
                            packageName);
                    loaders.put(container, loader);
                    log.info("Found module loader for " + container.getModId() + " mod.");
                    return loader;
                }
            loaders.put(container, null);
        }
        return null;
    }

    @Hook
    public static void handleModStateEvent(FMLModContainer instance, FMLEvent event) {
        LoadController controller = UtilsReflection.getPrivateValueUpTo(FMLModContainer.class, instance, ModContainer.class, "controller");
        try {
            ModuleLoader loader = getOrTryCreateLoader(instance);
            if (loader != null) {
                if (event instanceof FMLConstructionEvent)
                    loader.findModules(((FMLConstructionEvent) event).getASMHarvestedData());
                loader.listen(event);
            }
        } catch (Throwable t) {
            controller.errorOccurred(instance, t);
        }
    }
}
