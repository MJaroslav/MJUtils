package io.github.mjaroslav.mjutils.util.game;

import io.github.mjaroslav.mjutils.modular.ModuleLoader;
import io.github.mjaroslav.mjutils.modular.Proxy;
import io.github.mjaroslav.mjutils.modular.SubscribeLoader;
import io.github.mjaroslav.mjutils.util.io.UtilsFiles;
import io.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.ReflectionHelper;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

@Log4j2
@UtilityClass
public class UtilsMods {
    public @NotNull ModContainer getActiveMod() {
        return Loader.instance().activeModContainer();
    }

    public @NotNull String getActiveModId() {
        return Loader.instance().activeModContainer().getModId();
    }

    public boolean isModsLoaded(String @NotNull ... modIds) {
        for (String modId : modIds)
            if (!Loader.isModLoaded(modId))
                return false;
        return true;
    }

    public @Nullable ModContainer getContainer(@Nullable String modId) {
        Optional<ModContainer> optional = Loader.instance().getActiveModList().stream()
                .filter(mod -> mod.getModId().equals(modId)).findFirst();
        return optional.orElse(null);
    }

    public @Nullable Proxy getProxyModuleFromMod(@NotNull Object modInstance) {
        return (Proxy) getProxyObjectFromMod(modInstance);
    }


    public @Nullable Object getProxyObjectFromMod(@NotNull Object modInstance) {
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

    private final Map<ModContainer, ModuleLoader> loaders = new HashMap<>();


    public @Nullable ModuleLoader getActiveLoader() {
        ModContainer mc = Loader.instance().activeModContainer();
        return mc instanceof FMLModContainer ? getOrTryCreateModuleLoader(mc, false) : null;
    }


    public @Nullable ModuleLoader getOrTryCreateModuleLoader(@NotNull ModContainer container, boolean create) {
        if (loaders.containsKey(container))
            return loaders.get(container);
        else if (create) {
            Object modInstance = container.getMod();
            for (Field field : modInstance.getClass().getFields())
                if (field.isAnnotationPresent(SubscribeLoader.class)) {
                    String packageName = field.getAnnotation(SubscribeLoader.class).value();
                    if (packageName.isEmpty())
                        packageName = UtilsReflection.getPackageFromClass(modInstance);
                    val isStatic = Modifier.isStatic(field.getModifiers());
                    ModuleLoader loader = new ModuleLoader(container.getModId(), modInstance, packageName);
                    try {
                        field.set(isStatic ? null : modInstance, loader);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e); // TODO: Add something for warning or else.
                    }
                    loaders.put(container, loader);
                    return loader;
                }
            loaders.put(container, null);
        }
        return null;
    }

    public @Nullable InputStream getResourceFromModIgnored(@Nullable ModContainer container, @NotNull String path,
                                                           boolean cacheJar) {
        try {
            return getResourceFromMod(container, path, cacheJar);
        } catch (IOException ignored) {
            return null;
        }
    }

    private final Map<Path, JarFile> openedZips = new HashMap<>();

    private @NotNull InputStream getResource(@NotNull String path) throws IOException {
        val slashedPath = !path.startsWith("/") ? "/" + path : path;
        val result = UtilsMods.class.getResourceAsStream(slashedPath);
        if (result == null)
            throw new IOException("Can't find " + slashedPath + " resource from all sources");
        return result;
    }

    public @NotNull InputStream getResourceFromMod(@Nullable ModContainer container,
                                                   @NotNull String path, boolean cacheJar) throws IOException {
        if (container == null)
            return getResource(path);
        else {
            path = path.startsWith("/") ? path.substring(1) : path;
            val source = getModSource(container);
            if (Files.isRegularFile(source)) {
                JarFile mod;
                boolean shouldClose = false;
                if (openedZips.containsKey(source))
                    mod = openedZips.get(source);
                else {
                    mod = new JarFile(source.toFile());
                    if (cacheJar)
                        openedZips.put(source, mod);
                    else shouldClose = true;
                }
                val entry = mod.getEntry(path);
                if (entry == null)
                    throw new IOException("Can't find " + path + " resource from " + source);
                if (shouldClose) {
                    val bytes = IOUtils.toByteArray(mod.getInputStream(entry));
                    mod.close();
                    return new ByteArrayInputStream(bytes);
                } else return mod.getInputStream(entry);
            } else if (Files.isDirectory(source))
                return Files.newInputStream(source.resolve(path));
            else throw new IOException("Source should be existed file or directory");
        }
    }

    @Getter
    private final Map<ModContainer, Path> containerSource = new HashMap<>();
    private final Map<ModContainer, ModState> states = new HashMap<>();
    @Getter
    private final Set<DisabledModContainer> disabledMods = new HashSet<>();

    public ModState getModState(ModContainer container) {
        return states.getOrDefault(container, ModState.INTERNAL);
    }

    public ModState changeModState(ModContainer container, boolean state) {
        val result = getModState(container).change(state);
        states.put(container, result);
        return result;
    }

    public @NotNull Path getModSource(@NotNull ModContainer container) {
        return containerSource.get(container);
    }

    public ModState toggleModState(ModContainer container) {
        val result = getModState(container).toggle();
        states.put(container, result);
        return result;
    }

    public boolean canChangeState(ModContainer container) {
        return getModState(container) != ModState.INTERNAL;
    }

    public boolean isModDisabled(ModContainer container) {
        return getModState(container).isDisabled();
    }

    public boolean isModEnabled(ModContainer container) {
        return getModState(container).isEnabled();
    }

    public boolean isModScheduled(ModContainer container) {
        return getModState(container).isScheduled();
    }

    public @NotNull File getMinecraftDir() {
        return (File) FMLInjectionData.data()[6];
    }

    public void init() {
        val modsDir = UtilsFiles.get(getMinecraftDir(), "mods");
        val versionModsDir = modsDir.resolve(Loader.MC_VERSION);

        val disabledMods = UtilsFiles.list(modsDir).filter(checkPath -> UtilsFiles.isExtension(checkPath,
                "disabled")).collect(Collectors.toSet());
        disabledMods.addAll(UtilsFiles.list(versionModsDir).filter(checkPath -> UtilsFiles.isExtension(checkPath,
                "disabled")).collect(Collectors.toSet()));

        Loader.instance().getModList().forEach(container -> {
            if (container instanceof InjectedModContainer) {
                val location = ((InjectedModContainer) container).wrappedContainer
                        .getClass().getProtectionDomain().getCodeSource().getLocation();
                if (location.getProtocol().equals("jar") || location.getProtocol().equals("file")) {
                    val file = UtilsFiles.normalizePath(location);
                    if (file != null) {
                        containerSource.put(container, file);
                        states.put(container, file.startsWith(modsDir) ? ModState.ENABLED : ModState.INTERNAL);
                    }
                }
            } else if (container instanceof FMLModContainer) {
                val location = UtilsFiles.normalizePath(container.getSource());
                containerSource.put(container, location);
                states.put(container, location.startsWith(modsDir) ? ModState.ENABLED : ModState.INTERNAL);
            }
        });

        disabledMods.forEach(path -> loadDisabledMods(path).forEach(container -> {
            containerSource.put(container, path);
            states.put(container, ModState.DISABLED);
            UtilsMods.disabledMods.add(container);
        }));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> states.forEach((container, state) -> {
            if (state.isScheduled()) {
                val path = containerSource.get(container);
                if (state.isEnabled())
                    UtilsFiles.replaceExtension(path, "jar");
                else
                    UtilsFiles.replaceExtension(path, "disabled");
            }
            openedZips.values().forEach(zip -> {
                try {
                    zip.close();
                } catch (IOException ignored) {
                }
            });
        })));
    }

    private @NotNull List<DisabledModContainer> loadDisabledMods(@NotNull Path path) {
        val result = new ArrayList<DisabledModContainer>();
        try (val zip = new ZipFile(path.toFile())) {
            val entry = zip.getEntry("mcmod.info");
            if (entry != null) {
                val mc = MetadataCollection.from(zip.getInputStream(entry),
                        path.getFileName().toString());
                ModMetadata[] modList = ReflectionHelper.getPrivateValue(MetadataCollection.class, mc, "modList");
                for (var mod : modList)
                    result.add(new DisabledModContainer(mod, path));
            } else {
                val md = new ModMetadata();
                md.name = path.getFileName().toString();
                md.version = "UNKNOWN";
                md.modId = path.getFileName().toString();
                result.add(new DisabledModContainer(md, path));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public class DisabledModContainer extends DummyModContainer {
        private final Path source;

        public DisabledModContainer(ModMetadata modMetadata, Path source) {
            super(modMetadata);
            modMetadata.screenshots = new String[0];
            modMetadata.logoFile = "";
            this.source = source;
        }

        @Override
        public File getSource() {
            return source.toFile();
        }
    }

    public enum ModState {
        ENABLED, DISABLED, ENABLED_SCHEDULED, DISABLED_SCHEDULED, INTERNAL;

        public boolean isEnabled() {
            return this == ENABLED || this == ENABLED_SCHEDULED;
        }

        public boolean isDisabled() {
            return this == DISABLED || this == DISABLED_SCHEDULED;
        }

        public boolean isScheduled() {
            return this == ENABLED_SCHEDULED || this == DISABLED_SCHEDULED;
        }

        public ModState change(boolean enable) {
            return switch (this) {
                case DISABLED_SCHEDULED, ENABLED -> enable ? ENABLED : DISABLED_SCHEDULED;
                case ENABLED_SCHEDULED, DISABLED -> enable ? ENABLED_SCHEDULED : DISABLED;
                default -> INTERNAL;
            };
        }

        public ModState toggle() {
            return switch (this) {
                case ENABLED -> DISABLED_SCHEDULED;
                case DISABLED -> ENABLED_SCHEDULED;
                case ENABLED_SCHEDULED -> DISABLED;
                case DISABLED_SCHEDULED -> ENABLED;
                default -> INTERNAL;
            };
        }
    }
}
