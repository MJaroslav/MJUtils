package com.github.mjaroslav.mjutils.configurator.impl.configurator.json;

import blue.endless.jankson.JsonObject;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorEvents;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ManualJson5Configurator extends Json5ConfiguratorBase<JsonObject> {
    protected SyncCallback<JsonObject> syncFunc;
    protected ResourcePath defaultPath;

    public ManualJson5Configurator(@Nonnull ConfiguratorsLoader loader, @Nonnull String fileName, @Nonnull ResourcePath defaultPath) {
        super(loader, fileName);
        this.defaultPath = defaultPath;
    }

    public ManualJson5Configurator(@Nonnull ConfiguratorsLoader loader, @Nonnull String fileName, @Nonnull JsonObject defaultInstance) {
        super(loader, fileName);
        defaultJsonInstance = defaultInstance;
    }

    @Nonnull
    @Override
    public State restoreDefault() {
        if (isReadOnly())
            return State.READONLY;
        setInstance(getDefaultJsonInstance());
        return State.OK;
    }

    @Nullable
    @Override
    public JsonObject getInstance() {
        return getJsonInstance();
    }

    @Override
    public void setInstance(@Nonnull JsonObject value) {
        if (isUseEvents()) {
            ConfiguratorEvents.ConfiguratorInstanceChangedEvent event = ConfiguratorEvents.configuratorInstanceChangedEventPost(jsonInstance, value, false);
            if (!event.isCanceled()) {
                jsonInstance = (JsonObject) event.newInstance;
                hasChanges = true;
            }
        } else {
            jsonInstance = value;
            hasChanges = true;
        }
    }

    @Nonnull
    @Override
    public State sync() {
        return syncFunc != null && jsonInstance != null ? syncFunc.sync(jsonInstance) : State.OK;
    }

    @Nonnull
    @Override
    protected JsonObject createDefaultJsonInstance() {
        JsonObject loaded = loadJsonObject(defaultPath);
        if (loaded != null)
            return loaded;
        else
            throw new RuntimeException(String.format("Error on loading default configuration from \"%s\"", defaultPath));
    }

    public void setSyncCallback(SyncCallback<JsonObject> syncFunc) {
        this.syncFunc = syncFunc;
    }
}
