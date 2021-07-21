package com.github.mjaroslav.mjutils.configurator.impl.configurator.json;

import blue.endless.jankson.JsonObject;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ManualJson5Configurator extends Json5ConfiguratorBase {
    protected SyncCallback<JsonObject> syncFunc;
    protected ResourcePath defaultPath;
    protected JsonObject oldJsonInstance;

    public ManualJson5Configurator(@Nonnull String modId, @Nonnull String fileName, @Nonnull ResourcePath defaultPath) {
        super(modId, fileName);
        this.defaultPath = defaultPath;
    }

    public ManualJson5Configurator(@Nonnull String modId, @Nonnull String fileName, @Nonnull JsonObject defaultInstance) {
        super(modId, fileName);
        defaultJsonInstance = defaultInstance;
    }

    @Nullable
    @Override
    public JsonObject getJsonInstance() {
        if (!readOnly) {
            if (oldJsonInstance != null)
                hasChanges = !oldJsonInstance.equals(jsonInstance);
            oldJsonInstance = jsonInstance.clone();
        }
        return jsonInstance;
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

    @SuppressWarnings("unchecked")
    public <T extends ManualJson5Configurator> T withSyncCallback(SyncCallback<JsonObject> syncFunc) {
        this.syncFunc = syncFunc;
        return (T) this;
    }
}
