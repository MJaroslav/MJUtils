package com.github.mjaroslav.mjutils.configurator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Getter
public abstract class Configurator {
    @Nonnull
    protected final String fileName;
    @Nonnull
    protected final String fileExt;

    @Nonnull
    public String getFile() {
        return String.format("./config/%s.%s", fileName, fileExt);
    }

    public abstract void load();

    public abstract void save();

    public abstract void sync();
}
