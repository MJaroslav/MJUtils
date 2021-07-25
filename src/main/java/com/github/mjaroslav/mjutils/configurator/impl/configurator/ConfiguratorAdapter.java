package com.github.mjaroslav.mjutils.configurator.impl.configurator;

import com.github.mjaroslav.mjutils.configurator.Configurator;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ConfiguratorAdapter<T> implements Configurator<T> {
    @Nonnull
    protected final String FILE_NAME;
    @Nonnull
    protected final String FILE_EXT;
    @Nonnull
    protected final ConfiguratorsLoader LOADER;

    protected String name;
    protected boolean readOnly;
    protected boolean crashOnError;
    protected boolean hasChanges;
    protected boolean useEvents;

    public ConfiguratorAdapter(@Nonnull ConfiguratorsLoader loader, @Nonnull String fileName, @Nonnull String fileExt) {
        LOADER = loader;
        FILE_NAME = fileName;
        FILE_EXT = fileExt;
    }

    @Nonnull
    @Override
    public ConfiguratorsLoader getLoader() {
        return LOADER;
    }

    @Nonnull
    @Override
    public String getFile() {
        return String.format("./config/%s.%s", FILE_NAME, FILE_EXT);
    }

    @Nullable
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public boolean hasChanges() {
        return hasChanges;
    }

    public void makeDirty() {
        hasChanges = true;
    }

    @Override
    public boolean isUseEvents() {
        return useEvents;
    }

    @Override
    public boolean canCrashOnError() {
        return crashOnError;
    }

    public void makeCrashOnError() {
        crashOnError = true;
    }

    public void makeReadOnly() {
        readOnly = true;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void shouldUseEvents() {
        useEvents = true;
    }
}
