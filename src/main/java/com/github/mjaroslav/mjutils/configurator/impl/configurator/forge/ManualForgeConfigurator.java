package com.github.mjaroslav.mjutils.configurator.impl.configurator.forge;

import com.github.mjaroslav.mjutils.configurator.ConfiguratorEvents;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ManualForgeConfigurator extends ForgeConfiguratorBase<Configuration> {
    @Nonnull
    protected final String ACTUAL_VERSION;
    protected SyncCallback<Configuration> syncFunc;

    public ManualForgeConfigurator(@Nonnull ConfiguratorsLoader loader, @Nonnull String fileName, @Nonnull String actualVersion) {
        super(loader, fileName);
        ACTUAL_VERSION = actualVersion;
    }

    public ManualForgeConfigurator(@Nonnull ConfiguratorsLoader loader, @Nonnull String fileName) {
        this(loader, fileName, UNKNOWN_VERSION);
    }

    @Nullable
    @Override
    public Configuration getInstance() {
        return getConfigurationInstance();
    }

    @Override
    public void setInstance(@Nonnull Configuration value) {
        if (isUseEvents()) {
            ConfiguratorEvents.ConfiguratorInstanceChangedEvent event = ConfiguratorEvents.configuratorInstanceChangedEventPost(configurationInstance, value, false);
            if (!event.isCanceled()) {
                configurationInstance = (Configuration) event.newInstance;
                loadProperties(configurationInstance);
                hasChanges = true;
            }
        } else {
            configurationInstance = value;
            loadProperties(configurationInstance);
            hasChanges = true;
        }
    }

    @Nonnull
    @Override
    public State restoreDefault() {
        if (isReadOnly())
            return State.READONLY;
        setInstance(defaultConfigurationInstance);
        return State.OK;
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
