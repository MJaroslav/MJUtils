package com.github.mjaroslav.mjutils.hook;

import com.github.mjaroslav.mjutils.gloomyfolken.hooklib.asm.Hook;
import com.github.mjaroslav.mjutils.modular.Loader;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.util.UtilsReflection;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.*;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class HooksFMLModContainer {
    private static Map<String, Class<? extends FMLEvent>> lazyMethods;
    private static final Map<FMLModContainer, Set<ModuleLoaderMethod>> METHODS = new HashMap<>();

    private static Map<String, Class<? extends FMLEvent>> getMethods() {
        if (lazyMethods == null) {
            lazyMethods = new HashMap<>();
            lazyMethods.put("preInitialization", FMLPreInitializationEvent.class);
            lazyMethods.put("initialization", FMLInitializationEvent.class);
            lazyMethods.put("postInitialization", FMLPostInitializationEvent.class);
            lazyMethods.put("construction", FMLConstructionEvent.class);
            lazyMethods.put("serverStopping", FMLServerStoppingEvent.class);
            lazyMethods.put("serverStopped", FMLServerStoppedEvent.class);
            lazyMethods.put("serverAboutToStart", FMLServerAboutToStartEvent.class);
            lazyMethods.put("serverStarting", FMLServerStartingEvent.class);
            lazyMethods.put("serverStarted", FMLServerStartedEvent.class);
            lazyMethods.put("loadComplete", FMLLoadCompleteEvent.class);
            lazyMethods.put("communications", FMLInterModComms.IMCEvent.class);
        }
        return lazyMethods;
    }

    private static Set<ModuleLoaderMethod> getMethods(FMLModContainer instance) {
        if (!METHODS.containsKey(instance)) {
            METHODS.put(instance, new HashSet<>());
            try {
                // TODO: Try use AT and dependencies
                Method temp;
                Object modInstance = instance.getMod();
                if (modInstance != null) {
                    for (Field field : modInstance.getClass().getFields())
                        if (field.isAnnotationPresent(Loader.class)) {
                            // TODO: Make creating loader from class name from loader
                            ModuleLoader loader = new ModuleLoader(instance.getModId(), modInstance);
                            field.set(null, loader);
                            Class<?> loaderClass = loader.getClass();

                            for (Map.Entry<String, Class<? extends FMLEvent>> entry : getMethods().entrySet()) {
                                temp = loaderClass.getMethod(entry.getKey(), entry.getValue());
                                temp.setAccessible(true);
                                METHODS.get(instance).add(new ModuleLoaderMethod(entry.getValue(), loader, temp));
                            }

                            break;
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return METHODS.get(instance);
    }

    @Hook
    public static void handleModStateEvent(FMLModContainer instance, FMLEvent event) {
        LoadController controller = UtilsReflection.getPrivateValueUpTo(FMLModContainer.class, instance, ModContainer.class, "controller");
        try {
            for (ModuleLoaderMethod m : getMethods(instance))
                if (m.EVENT.equals(event.getClass()))
                    m.METHOD.invoke(m.INSTANCE, event);
        } catch (Throwable t) {
            controller.errorOccurred(instance, t);
        }
    }

    private static class ModuleLoaderMethod {
        @Nonnull
        private final Class<? extends FMLEvent> EVENT;
        @Nonnull
        private final ModuleLoader INSTANCE;
        @Nonnull
        private final Method METHOD;

        private ModuleLoaderMethod(@Nonnull Class<? extends FMLEvent> event, @Nonnull ModuleLoader instance, @Nonnull Method method) {
            EVENT = event;
            INSTANCE = instance;
            METHOD = method;
        }

        @Override
        public int hashCode() {
            return Objects.hash(EVENT, INSTANCE, METHOD);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof FMLEvent))
                return false;
            return EVENT.equals(o.getClass());
        }
    }
}
