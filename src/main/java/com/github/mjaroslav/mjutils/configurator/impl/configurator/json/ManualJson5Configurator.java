package com.github.mjaroslav.mjutils.configurator.impl.configurator.json;

import blue.endless.jankson.JsonObject;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ManualJson5Configurator extends Json5ConfiguratorBase {
    protected final SyncFunction SYNC_FUNC;
    protected ResourcePath defaultPath;
    protected JsonObject oldJsonInstance;

    public ManualJson5Configurator(@Nonnull String modId, @Nonnull String fileName, @Nonnull ResourcePath defaultPath, @Nullable SyncFunction syncFunc) {
        super(modId, fileName);
        SYNC_FUNC = syncFunc;
        this.defaultPath = defaultPath;
    }

    public ManualJson5Configurator(@Nonnull String modId, @Nonnull String fileName, @Nonnull JsonObject defaultInstance, @Nullable SyncFunction syncFunc) {
        super(modId, fileName);
        SYNC_FUNC = syncFunc;
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
        return SYNC_FUNC != null && jsonInstance != null ? SYNC_FUNC.sync(jsonInstance) : State.OK;
    }

    @Nonnull
    @Override
    protected JsonObject createDefaultJsonInstance() {
        JsonObject loaded = loadJsonObject(defaultPath);
        if (loaded != null)
            return loaded;
        else throw new RuntimeException(String.format("Error on loading default configuration from \"%s\"", defaultPath));
    }

    public interface SyncFunction {
        State sync(JsonObject instance);
    }
}
