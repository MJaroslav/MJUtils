package com.github.mjaroslav.mjutils.configurator.impl.configurator.forge;

import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;

public abstract class ManualForgeConfigurator extends ForgeConfiguratorBase {
    @Nonnull
    protected final String ACTUAL_VERSION;
    protected  SyncCallback<Configuration> syncFunc;

    public ManualForgeConfigurator(@Nonnull String modId, @Nonnull String fileName, @Nonnull String actualVersion) {
        super(modId, fileName);
        ACTUAL_VERSION = actualVersion;
    }

    public ManualForgeConfigurator(@Nonnull String modId, @Nonnull String fileName) {
        this(modId, fileName, UNKNOWN_VERSION);
    }

    @Nonnull
    @Override
    public String getActualVersion() {
        return ACTUAL_VERSION;
    }

    @Nonnull
    @Override
    public State sync() {
        return syncFunc.sync(getInstance());
    }

    @SuppressWarnings("unchecked")
    public <T extends ManualForgeConfigurator> T withSyncCallback(SyncCallback<Configuration> syncFunc) {
        this.syncFunc = syncFunc;
        return (T) this;
    }
}
