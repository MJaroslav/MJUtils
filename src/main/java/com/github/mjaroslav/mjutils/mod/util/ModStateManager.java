package com.github.mjaroslav.mjutils.mod.util;

import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipFile;

@UtilityClass
public class ModStateManager {
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

    private void searchDisabledModsInFolder(Path path, Set<Path> disabledMods) {
        try (val files = Files.list(path)) {
            val result = new HashSet<Path>();
            files.forEach(file -> {
                if (FilenameUtils.getExtension(file.getFileName().toString()).equalsIgnoreCase("disabled"))
                    disabledMods.add(file.normalize());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        val modsDir = Minecraft.getMinecraft().mcDataDir.toPath().resolve("mods").normalize().toAbsolutePath();
        val versionModsDir = modsDir.resolve(Loader.MC_VERSION).normalize().toAbsolutePath();
        System.out.println(modsDir);

        val disabledMods = new HashSet<Path>();

        searchDisabledModsInFolder(modsDir, disabledMods);
        searchDisabledModsInFolder(versionModsDir, disabledMods);

        Loader.instance().getModList().forEach(container -> {
            if (container instanceof InjectedModContainer) {
                val location = ((InjectedModContainer) container).wrappedContainer
                        .getClass().getProtectionDomain().getCodeSource().getLocation();
                if (location.getProtocol().equals("jar")) {
                    try {
                        val file = Paths.get(((JarURLConnection) location.openConnection())
                                .getJarFileURL().getFile()).normalize().toAbsolutePath();
                        containerSource.put(container, file);
                        states.put(container, file.startsWith(modsDir) ? ModState.ENABLED : ModState.INTERNAL);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (container instanceof FMLModContainer) {
                val location = container.getSource().toPath().normalize().toAbsolutePath();
                containerSource.put(container, location);
                states.put(container, location.startsWith(modsDir) ? ModState.ENABLED : ModState.INTERNAL);
            }
        });

        disabledMods.forEach(path -> loadDisabledMods(path).forEach(container -> {
            containerSource.put(container, path);
            states.put(container, ModState.DISABLED);
            ModStateManager.disabledMods.add(container);
        }));

        containerSource.forEach((container, source) -> System.out.println(container.getModId() + " " + source));
        states.forEach((container, state) -> System.out.println(container.getModId() + " " + state));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> states.forEach((container, state) -> {
            if (state.isScheduled()) {
                val path = containerSource.get(container);
                if (state.isEnabled())
                    try {
                        Files.move(path, path.getParent().resolve(FilenameUtils.removeExtension(
                                path.getFileName().toString())));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                else
                    try {
                        Files.move(path, path.getParent().resolve(path.getFileName() + ".disabled"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }
        })));
    }

    private List<DisabledModContainer> loadDisabledMods(@NotNull Path path) {
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

    public static class DisabledModContainer extends DummyModContainer {
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
            switch (this) {
                case DISABLED_SCHEDULED:
                case ENABLED:
                    return enable ? ENABLED : DISABLED_SCHEDULED;
                case ENABLED_SCHEDULED:
                case DISABLED:
                    return enable ? ENABLED_SCHEDULED : DISABLED;
                default:
                    return INTERNAL;
            }
        }

        public ModState toggle() {
            switch (this) {
                case ENABLED:
                    return DISABLED_SCHEDULED;
                case DISABLED:
                    return ENABLED_SCHEDULED;
                case ENABLED_SCHEDULED:
                    return DISABLED;
                case DISABLED_SCHEDULED:
                    return ENABLED;
                default:
                    return INTERNAL;
            }
        }
    }
}
