package com.github.mjaroslav.mjutils.util.game;

import com.github.mjaroslav.mjutils.configurator.PropertiesConfigurator;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.modular.Proxy;
import com.github.mjaroslav.mjutils.modular.SubscribeLoader;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import com.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import cpw.mods.fml.common.*;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

@Log4j2
public class UtilsMods {
    private static Set<String> modsWithCoreMods;
    private static final PropertiesConfigurator fmlModStateProps;
    public static final Set<String> blockedForDisableMods = new HashSet<>();

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
        return mc instanceof FMLModContainer ? getOrTryCreateModuleLoader((FMLModContainer) mc, false) : null;
    }

    @Nullable
    public static ModuleLoader getOrTryCreateModuleLoader(@Nonnull FMLModContainer container, boolean create) {
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
    public static InputStream getResourceFromModAsStream(String modId, String path) {
        try {
            ModContainer mod = getContainer(modId);
            if (mod != null) {
                File source = mod.getSource();
                if (source.isFile()) {
                    JarFile jar = new JarFile(source);
                    ZipEntry entry = jar.getEntry(path);
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

    public static boolean canDisableMod(String modId) {
        if (modsWithCoreMods == null) {
            modsWithCoreMods = new HashSet<>();
            modsWithCoreMods.add("mcp");
            for (ModContainer mod : Loader.instance().getActiveModList()) {
                File source = mod.getSource();
                try {
                    Attributes attribs;
                    if (source.isFile())
                        attribs = new JarFile(source).getManifest().getMainAttributes();
                    else
                        attribs = new Manifest(Files.newInputStream(Paths.get(source.toURI())
                                .resolve("META-INF/MANIFEST.MF"))).getMainAttributes();
                    if (attribs.getValue("FMLCorePluginContainsFMLMod") != null)
                        modsWithCoreMods.add(mod.getModId());
                } catch (Exception ignored) {
                }
            }
        }
        return !modsWithCoreMods.contains(modId) && !blockedForDisableMods.contains(modId);
    }


    public static boolean getActualModState(ModContainer mc) {
        return Loader.instance().getModState(mc) != LoaderState.ModState.DISABLED;
    }

    public static boolean getSavedModState(String modId) {
        return Boolean.parseBoolean(fmlModStateProps.getProperties().getProperty(modId, "true"));
    }

    public static void setSavedModState(String modId, boolean value) {
        fmlModStateProps.getProperties().setProperty(modId, String.valueOf(value));
    }

    public static boolean toggleSavedModState(String modId) {
        boolean value = !getSavedModState(modId);
        setSavedModState(modId, value);
        fmlModStateProps.save();
        return value;
    }

    static {
        fmlModStateProps = new PropertiesConfigurator("fmlModState",
                ResourcePath.of("mjutils:configurators/fmlModState.properties"));
        fmlModStateProps.load();
    }
}
