package com.github.mjaroslav.mjutils.configurator;

import cpw.mods.fml.common.Loader;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

@Getter
public abstract class Configurator {
    protected final @NotNull String fileName;
    protected final @NotNull String fileExt;
    protected final @NotNull Path file;

    public Configurator(@NotNull String fileName, @NotNull String fileExt) {
        this.fileName = fileName;
        this.fileExt = fileExt;
        file = Loader.instance().getConfigDir().toPath().resolve(fileName + "." + fileExt);
    }

    public abstract void load();

    public abstract void save();
}
