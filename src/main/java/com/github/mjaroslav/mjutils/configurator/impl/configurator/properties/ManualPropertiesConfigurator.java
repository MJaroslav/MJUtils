package com.github.mjaroslav.mjutils.configurator.impl.configurator.properties;

import com.github.mjaroslav.mjutils.configurator.ConfiguratorEvents;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorEvents.ConfiguratorInstanceChangedEvent;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ManualPropertiesConfigurator extends PropertiesConfiguratorBase<Properties> {
    @Nonnull
    protected ResourcePath defaultPath;
    protected SyncCallback<Properties> syncFunc;

    public ManualPropertiesConfigurator(@Nonnull ConfiguratorsLoader loader, @Nonnull String fileName, @Nonnull ResourcePath defaultPath) {
        super(loader, fileName);
        this.defaultPath = defaultPath;
    }

    @Nullable
    @Override
    public Properties getInstance() {
        return getPropertiesInstance();
    }

    @Override
    public void setInstance(@Nonnull Properties value) {
        if (isUseEvents()) {
            ConfiguratorInstanceChangedEvent event = ConfiguratorEvents.configuratorInstanceChangedEventPost(getPropertiesInstance(), value, false);
            if (!event.isCanceled()) {
                propertiesInstance = (Properties) event.newInstance;
                hasChanges = true;
            }
        } else {
            propertiesInstance = value;
            hasChanges = true;
        }
    }

    @Nonnull
    @Override
    public String getActualVersion() {
        return getDefaultPropertiesInstance().getProperty(VERSION_KEY, UNKNOWN_VERSION);
    }

    @Nonnull
    @Override
    public State restoreDefault() {
        setInstance(getDefaultPropertiesInstance());
        return State.OK;
    }

    @Nonnull
    @Override
    public State sync() {
        return syncFunc != null && propertiesInstance != null ? syncFunc.sync(propertiesInstance) : State.OK;
    }


    @Nonnull
    @Override
    protected Properties createDefaultPropertiesInstance() {
        Properties result = new Properties();
        InputStream io = defaultPath.stream();
        if (io == null) {
            result.setProperty(VERSION_KEY, UNKNOWN_VERSION);
        } else {
            try {
                result.load(io);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(String.format("Error on loading default configuration from \"%s\"", defaultPath));
            }
        }
        return result;
    }

    public void setSyncCallback(SyncCallback<Properties> syncFunc) {
        this.syncFunc = syncFunc;
    }
}
