package com.github.mjaroslav.mjutils.configurator.impl.configurator;

import com.github.mjaroslav.mjutils.configurator.Configurator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ConfiguratorAdapter implements Configurator {
    @Nonnull
    protected final String MOD_ID;
    @Nonnull
    protected final String FILE_NAME;
    @Nonnull
    protected final String FILE_EXT;

    protected String name;
    protected boolean readOnly;
    protected boolean crashOnError;

    public ConfiguratorAdapter(@Nonnull String modId, @Nonnull String fileName, @Nonnull String fileExt) {
        MOD_ID = modId;
        FILE_NAME = fileName;
        FILE_EXT = fileExt;
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

    @Nonnull
    @Override
    public String getModId() {
        return MOD_ID;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public boolean canCrashOnError() {
        return crashOnError;
    }

    @SuppressWarnings("unchecked")
    public <T extends ConfiguratorAdapter> T makeCrashOnError() {
        crashOnError = true;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends ConfiguratorAdapter> T makeReadOnly() {
        readOnly = true;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends ConfiguratorAdapter> T withName(String name) {
        this.name = name;
        return (T) this;
    }
}
