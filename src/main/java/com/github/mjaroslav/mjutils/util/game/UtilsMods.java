package com.github.mjaroslav.mjutils.util.game;

import com.github.mjaroslav.mjutils.mod.util.ModStateManager;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.modular.Proxy;
import com.github.mjaroslav.mjutils.modular.SubscribeLoader;
import com.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import cpw.mods.fml.common.*;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

@Log4j2
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
    public static ModContainer getContainer(String modId) {
        Optional<ModContainer> optional = Loader.instance().getActiveModList().stream()
                .filter(mod -> mod.getModId().equals(modId)).findFirst();
        return optional.orElse(null);
    }

    @Nullable
    public static Proxy getProxyModuleFromMod(@NotNull Object modInstance) {
        return (Proxy) getProxyObjectFromMod(modInstance);
    }

    @Nullable
    public static Object getProxyObjectFromMod(@NotNull Object modInstance) {
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

    private static final Map<ModContainer, ModuleLoader> loaders = new HashMap<>();

    @Nullable
    public static ModuleLoader getActiveLoader() {
        ModContainer mc = Loader.instance().activeModContainer();
        return mc instanceof FMLModContainer ? getOrTryCreateModuleLoader(mc, false) : null;
    }

    @Nullable
    public static ModuleLoader getOrTryCreateModuleLoader(@NotNull ModContainer container, boolean create) {
        if (loaders.containsKey(container))
            return loaders.get(container);
        else if (create) {
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
        }
        return null;
    }

    @Nullable
    public static InputStream getResourceFromModAsStream(@NotNull String modId, @NotNull String path) {
        try {
            ModContainer mod = getContainer(modId);
            if (mod != null) {
                File source = mod.getSource();
                String absPath = !path.startsWith("/") ? "/" + path : path;
                if (source.toString().equals("minecraft.jar")) // Lol
                    return UtilsMods.class.getResourceAsStream(absPath);
                else if (source.isFile()) {
                    JarFile jar = new JarFile(source);
                    ZipEntry entry = jar.getEntry(absPath);
                    return entry == null ? null : jar.getInputStream(entry);
                } else {
                    return Files.newInputStream(source.toPath().resolve(path));
                }
            }
        } catch (NoSuchFileException ignored) {
            return null;
        } catch (Exception e) {
            log.error(String.format("Can't load resource '%s' from '%s' mod", modId, path), e);
        }
        return null;
    }

    public static boolean canDisableMod(ModContainer mc) {
        return ModStateManager.canChangeState(mc.getSource().toPath());
    }

    public static boolean getActualModState(ModContainer mc) {
        return Loader.instance().getModState(mc) != LoaderState.ModState.DISABLED;
    }

    public static boolean getSavedModState(ModContainer mc) {
        return !ModStateManager.isModDisabled(mc.getSource().toPath());
    }

    public static void setSavedModState(ModContainer mc, boolean value) {
        ModStateManager.changeModState(mc.getSource().toPath(), value);
    }

    public static boolean toggleSavedModState(ModContainer mc) {
        boolean value = !getSavedModState(mc);
        setSavedModState(mc, value);
        return value;
    }
}
