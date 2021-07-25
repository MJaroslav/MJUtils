package com.github.mjaroslav.mjutils.configurator.impl.configurator.json;

import blue.endless.jankson.JsonObject;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorEvents;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

public class GenericJson5Configurator<T> extends Json5ConfiguratorBase<T> {
    protected SyncCallback<T> syncFunc;
    protected Class<T> lazyLoadedTypeClass;
    protected T defaultGenericInstance;
    protected T genericInstance;
    protected ResourcePath defaultPath;

    public GenericJson5Configurator(@Nonnull ConfiguratorsLoader loader, @Nonnull String fileName, ResourcePath defaultPath) {
        super(loader, fileName);
        this.defaultPath = defaultPath;
    }

    public GenericJson5Configurator(@Nonnull ConfiguratorsLoader loader, @Nonnull String fileName, T defaultInstance) {
        super(loader, fileName);
        defaultGenericInstance = defaultInstance;
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getTypeClass() {
        if (lazyLoadedTypeClass == null)
            lazyLoadedTypeClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return lazyLoadedTypeClass;
    }

    @Nullable
    @Override
    public T getInstance() {
        return genericInstance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setInstance(@Nonnull T value) {
        if (isUseEvents()) {
            ConfiguratorEvents.ConfiguratorInstanceChangedEvent event = ConfiguratorEvents.configuratorInstanceChangedEventPost(genericInstance, value, false);
            if (!event.isCanceled()) {
                genericInstance = (T) event.newInstance;
                jsonInstance = (JsonObject) JANKSON.toJson(genericInstance);
                hasChanges = true;
            }
        } else {
            genericInstance = value;
            jsonInstance = (JsonObject) JANKSON.toJson(genericInstance);
            hasChanges = true;
        }
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
    public State restoreDefault() {
        if (isReadOnly())
            return State.READONLY;
        setInstance(defaultGenericInstance);
        return State.OK;
    }

    @Nonnull
    @Override
    public State sync() {
        return syncFunc != null && jsonInstance != null ? syncFunc.sync(genericInstance) : State.OK;
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

    public void setSyncCallback(SyncCallback<T> syncFunc) {
        this.syncFunc = syncFunc;
    }
}