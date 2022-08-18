package com.github.mjaroslav.mjutils.mod.util;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModMetadata;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

@UtilityClass
public class ModStateManager {
    private final Set<Path> modsForEnabling = new HashSet<>();
    private final Set<Path> modsForDisabling = new HashSet<>();
    private final Set<Path> allowedMods = new HashSet<>();
    private final Set<Path> disabledMods = new HashSet<>();
    @Getter
    private final Set<DisabledModContainer> disabledModsContainers = new HashSet<>();

    public boolean isModPreparedForDisabling(@NotNull Path file) {
        return modsForDisabling.contains(file.toAbsolutePath());
    }

    public boolean isModPreparedForEnabling(@NotNull Path file) {
        return modsForEnabling.contains(file.toAbsolutePath());
    }

    public void changeModState(@NotNull Path file, boolean state) {
        if (allowedMods.contains(file.toAbsolutePath()))
            if (state && isModDisabled(file.toAbsolutePath())) {
                modsForEnabling.add(file.toAbsolutePath());
                modsForDisabling.remove(file.toAbsolutePath());
            } else if (!state && !isModDisabled(file.toAbsolutePath())) {
                modsForDisabling.add(file.toAbsolutePath());
                modsForEnabling.remove(file.toAbsolutePath());
            }
    }

    public boolean canChangeState(@NotNull Path file) {
        return allowedMods.contains(file.toAbsolutePath());
    }

    public boolean isModDisabled(@NotNull Path file) {
        return disabledMods.contains(file.toAbsolutePath());
    }

    public void init() {
        val modsDir = Minecraft.getMinecraft().mcDataDir.toPath().resolve("mods");
        val versionModsDir = modsDir.resolve(Loader.MC_VERSION);
        try (val files = Files.list(modsDir)) {
            files.forEach(path -> {
                if (Files.isRegularFile(path)) {
                    val ext = FilenameUtils.getExtension(path.getFileName().toString());
                    if (ext.equals("jar") || ext.equals("disabled"))
                        allowedMods.add(path.toAbsolutePath());
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        disabledMods.addAll(allowedMods.stream().filter(path ->
                        FilenameUtils.isExtension(path.getFileName().toString(), "disabled"))
                .collect(Collectors.toSet()));
        disabledMods.forEach(mod -> loadDisabledMod(mod));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            modsForDisabling.forEach(path -> {
                try {
                    Files.move(path, path.getParent().resolve(path.getFileName() + ".disabled"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            modsForEnabling.forEach(path -> {
                try {
                    Files.move(path, path.getParent().resolve(FilenameUtils.removeExtension(
                            path.getFileName().toString())));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }));
    }

    private void loadDisabledMod(@NotNull Path path) {
        try (val zip = new ZipFile(path.toFile())) {
            val entry = zip.getEntry("mcmod.info");
            if (entry != null) {
                val mc = MetadataCollection.from(zip.getInputStream(entry),
                        path.getFileName().toString());
                ModMetadata[] modList = ReflectionHelper.getPrivateValue(MetadataCollection.class, mc, "modList");
                for (var mod : modList)
                    disabledModsContainers.add(new DisabledModContainer(mod, path));
            } else {
                val md = new ModMetadata();
                md.name = path.getFileName().toString();
                md.version = "UNKNOWN";
                md.modId = "UNKNOWN";
                disabledModsContainers.add(new DisabledModContainer(md, path));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}
