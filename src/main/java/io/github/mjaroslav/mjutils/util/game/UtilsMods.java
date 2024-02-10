package io.github.mjaroslav.mjutils.util.game;

import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.ReflectionHelper;
import io.github.mjaroslav.mjutils.modular.Proxy;
import io.github.mjaroslav.sharedjava.io.PathFiles;
import io.github.mjaroslav.sharedjava.tuple.Pair;
import io.github.mjaroslav.sharedjava.tuple.pair.SimplePair;
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

import static io.github.mjaroslav.mjutils.lib.MJUtilsInfo.*;

@Log4j2
@UtilityClass
public class UtilsMods {
    public @NotNull String getActiveModNameFormatted() {
        val mod = getActiveMod();
        return String.format("%s (%s) mod", mod.getName(), mod.getModId());
    }

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

    public @Nullable Pair<Proxy, Field> getFirstProxyObjectFromMod(@NotNull Object modInstance) {
        return getProxyObjectsFromMod(modInstance).stream().findFirst().orElse(null);
    }

    public @NotNull List<Pair<Proxy, Field>> getProxyObjectsFromMod(@NotNull Object modInstance) {
        return getSidedOnlyObjectsFromMod(modInstance).stream().filter(pair -> pair.getX() instanceof Proxy)
            .map(pair -> new SimplePair<>((Proxy) pair.getX(), pair.getY()))
            .collect(Collectors.toList());
    }

    public @Nullable Pair<Object, Field> getFirstSidedOnlyObjectFromMod(@NotNull Object modInstance) {
        return getSidedOnlyObjectsFromMod(modInstance).stream().findFirst().orElse(null);
    }

    public @NotNull List<Pair<Object, Field>> getSidedOnlyObjectsFromMod(@NotNull Object modInstance) {
        int mods;
        val className = modInstance.getClass().getName();
        val result = new ArrayList<Pair<Object, Field>>();
        for (val field : modInstance.getClass().getFields()) {
            mods = field.getModifiers();
            if (field.isAnnotationPresent(SidedProxy.class))
                if (Modifier.isStatic(mods) && Modifier.isPublic(mods)) {
                    try {
                        result.add(new SimplePair<>(field.get(null), field));
                    } catch (IllegalAccessException e) {
                        LOG_LIB.error(String.format("Can't get SidedOnly object from %s#%s field", className, field.getName()), e);
                    }
                } else LOG_LIB.warn(String.format("Ignoring %s#%s field as SidedOnly object because its must be " +
                    "public and static", className, field.getName()));
        }
        return result;
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
        val modsDir = PathFiles.get(getMinecraftDir(), "mods");
        val versionModsDir = modsDir.resolve(Loader.MC_VERSION);

        val disabledMods = PathFiles.list(modsDir).filter(checkPath -> PathFiles.isExtension(checkPath,
            "disabled")).collect(Collectors.toSet());
        disabledMods.addAll(PathFiles.list(versionModsDir).filter(checkPath -> PathFiles.isExtension(checkPath,
            "disabled")).collect(Collectors.toSet()));

        Loader.instance().getModList().forEach(container -> {
            if (container instanceof InjectedModContainer) {
                val location = ((InjectedModContainer) container).wrappedContainer
                    .getClass().getProtectionDomain().getCodeSource().getLocation();
                if (location.getProtocol().equals("jar") || location.getProtocol().equals("file")) {
                    val file = PathFiles.normalizePath(location);
                    if (file != null) {
                        containerSource.put(container, file);
                        states.put(container, file.startsWith(modsDir) ? ModState.ENABLED : ModState.INTERNAL);
                    }
                }
            } else if (container instanceof FMLModContainer) {
                val location = PathFiles.normalizePath(container.getSource());
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
                    PathFiles.replaceExtension(path, "jar");
                else
                    PathFiles.replaceExtension(path, "disabled");
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
