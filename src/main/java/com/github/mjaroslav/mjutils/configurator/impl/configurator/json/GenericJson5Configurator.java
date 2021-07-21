package com.github.mjaroslav.mjutils.configurator.impl.configurator.json;

import blue.endless.jankson.JsonObject;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

public class GenericJson5Configurator<T> extends Json5ConfiguratorBase {
    protected SyncCallback<T> syncFunc;
    protected Class<T> lazyLoadedTypeClass;
    protected T defaultGenericInstance;
    protected T genericInstance;
    protected ResourcePath defaultPath;
    // TODO: Make normal checking for changes.
    protected T oldGenericInstance;

    public GenericJson5Configurator(@Nonnull String modId, @Nonnull String fileName, ResourcePath defaultPath) {
        super(modId, fileName);
        this.defaultPath = defaultPath;
    }

    public GenericJson5Configurator(@Nonnull String modId, @Nonnull String fileName, T defaultInstance) {
        super(modId, fileName);
        defaultGenericInstance = defaultInstance;
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getTypeClass() {
        if (lazyLoadedTypeClass == null)
            lazyLoadedTypeClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return lazyLoadedTypeClass;
    }

    public T getGenericInstance() {
        if (!readOnly) {
            if (oldGenericInstance != null) {
                JsonObject old = (JsonObject) JANKSON.toJson(oldGenericInstance);
                JsonObject current = (JsonObject) JANKSON.toJson(genericInstance);
                hasChanges = !old.equals(current);
            }
            oldGenericInstance = genericInstance;
        }
        return genericInstance;
    }

    @Nonnull
    @Override
    public State load() {
        // TODO: Try make autosync with jsonInstance or remake structure for independence from its.
        if (super.load() == State.OK) {
            genericInstance = JANKSON.fromJson(jsonInstance, getTypeClass());
            return State.OK;
        } else return State.ERROR;
    }

    @Nonnull
    @Override
    public State sync() {
        return syncFunc != null && jsonInstance != null ? syncFunc.sync(getGenericInstance()) : State.OK;
    }

    @Nonnull
    @Override
    protected JsonObject createDefaultJsonInstance() {
        if (defaultGenericInstance != null) {
            defaultJsonInstance = (JsonObject) JANKSON.toJson(defaultGenericInstance);
            // TODO: Check for static field parsing in Jankson.
            int mods;
            for (Field field : getTypeClass().getDeclaredFields()) {
                mods = field.getModifiers();
                if (Modifier.isStatic(mods) && Modifier.isPublic(mods) && Modifier.isFinal(mods) &&
                        field.getName().equals(VERSION_KEY.toUpperCase()) && field.getType() == String.class) {
                    try {
                        defaultJsonInstance.putDefault(VERSION_KEY, (String) field.get(null), "Configuration version. DON'T CHANGE.");
                        break;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return defaultJsonInstance;
        } else {
            JsonObject loaded = loadJsonObject(defaultPath);
            if (loaded != null) {
                defaultGenericInstance = JANKSON.fromJson(loaded, getTypeClass());
                return loaded;
            } else
                throw new RuntimeException(String.format("Error on loading default configuration from \"%s\"", defaultPath));
        }
    }

    @SuppressWarnings("unchecked")
    public <E extends GenericJson5Configurator<T>> E  withSyncCallback(SyncCallback<T> syncFunc) {
        this.syncFunc = syncFunc;
        return (E) this;
    }
}