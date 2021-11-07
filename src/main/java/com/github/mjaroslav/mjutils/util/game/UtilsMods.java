package com.github.mjaroslav.mjutils.util.game;

import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.modular.Proxy;
import com.github.mjaroslav.mjutils.modular.SubscribeLoader;
import com.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class UtilsMods {
    public static String getActiveModId() {
        return Loader.instance().activeModContainer().getModId();
    }

    public static boolean isModsLoaded(String... modIds) {
        for (String modId : modIds)
            if (!Loader.isModLoaded(modId))
                return false;
        return true;
    }

    @Nullable
    public static Proxy getProxyModuleFromMod(@Nonnull Object modInstance) {
        return (Proxy) getProxyObjectFromMod(modInstance);
    }

    @Nullable
    public static Object getProxyObjectFromMod(@Nonnull Object modInstance) {
        int mods;
        for (Field field : modInstance.getClass().getFields()) {
            mods = field.getModifiers();
            if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && field.isAnnotationPresent(SidedProxy.class)) {
                try {
                    return field.get(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    private static final Map<FMLModContainer, ModuleLoader> loaders = new HashMap<>();

    @Nullable
    public static ModuleLoader getActiveLoader() {
        ModContainer mc = Loader.instance().activeModContainer();
        return mc instanceof FMLModContainer ? getOrTryCreateModuleLoader((FMLModContainer) mc) : null;
    }

    @Nullable
    public static ModuleLoader getOrTryCreateModuleLoader(@Nonnull FMLModContainer container) {
        if (loaders.containsKey(container))
            return loaders.get(container);
        else {
            Object modInstance = container.getMod();
            for (Field field : modInstance.getClass().getFields())
                if (field.isAnnotationPresent(SubscribeLoader.class)) {
                    String packageName = field.getAnnotation(SubscribeLoader.class).value();
                    if (packageName.isEmpty())
                        packageName = UtilsReflection.getPackageFromClass(modInstance);
                    ModuleLoader loader = new ModuleLoader(container.getModId(), modInstance, packageName);
                    loaders.put(container, loader);
                    return loader;
                }
            loaders.put(container, null);
            return null;
        }
    }
}
