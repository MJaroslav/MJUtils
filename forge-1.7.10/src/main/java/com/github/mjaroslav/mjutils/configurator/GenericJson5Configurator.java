package com.github.mjaroslav.mjutils.configurator;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.stream.Collectors;

public class GenericJson5Configurator<T> extends Configurator {
    public static final Jankson JANKSON = Jankson.builder().build();
    protected JsonObject jsonInstance;
    protected JsonObject defaultJsonInstance;

    @Nonnull
    @Getter
    protected final ResourcePath defaultPath;

    @Nullable
    protected T valueSnapshot;

    protected Class<T> lazyLoadedTypeClass;

    public boolean genericToJsonObject = false;

    public GenericJson5Configurator(@Nonnull String fileName, @Nonnull ResourcePath defaultPath) {
        super(fileName, "json5");
        this.defaultPath = defaultPath;
        defaultJsonInstance = loadJsonObject(defaultPath);
    }

    @Nullable
    public T getValueSnapshot() {
        genericToJsonObject = false;
        sync();
        return valueSnapshot;
    }

    public void setValueSnapshot(@Nullable T valueSnapshot) {
        this.valueSnapshot = valueSnapshot;
        genericToJsonObject = true;
        sync();
    }

    @Nullable
    public String getLocalVersion() {
        if (jsonInstance == null)
            return null;
        return jsonInstance.get(String.class, "config_version");
    }

    @Nullable
    public String getActualVersion() {
        if (defaultJsonInstance == null)
            return null;
        return defaultJsonInstance.get(String.class, "config_version");
    }

    @Override
    public void load() {
        Path path = Paths.get(getFile());
        if (!Files.isRegularFile(path))
            restoreDefault();
        else {
            try {
                jsonInstance = JANKSON.load(Files.newInputStream(path));
                if (!StringUtils.equals(getLocalVersion(), getActualVersion())) {
                    Path pathForRename = Paths.get(getFile() + ".old");
                    Files.move(path, pathForRename, StandardCopyOption.REPLACE_EXISTING);
                    restoreDefault();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save() {
        if (jsonInstance == null)
            return;
        String json = jsonInstance.toJson(true, true);
        try {
            Files.createDirectories(Paths.get(getFile()).getParent());
            Files.write(Paths.get(getFile()), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getTypeClass() {
        if (lazyLoadedTypeClass == null)
            lazyLoadedTypeClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return lazyLoadedTypeClass;
    }

    @Override
    public void sync() {
        if (genericToJsonObject)
            jsonInstance = (JsonObject) JANKSON.toJson(valueSnapshot);
        else
            valueSnapshot = JANKSON.fromJson(jsonInstance, getTypeClass());
    }

    public void restoreDefault() {
        jsonInstance = loadJsonObject(defaultPath);
        genericToJsonObject = false;
        sync();
        try {
            Files.createDirectories(Paths.get(getFile()).getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = "";
        BufferedReader reader = defaultPath.bufferedReader();
        if(reader != null)
            result = reader.lines().collect(Collectors.joining("\n"));
        try {
            Files.write(Paths.get(getFile()), Collections.singleton(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static JsonObject loadJsonObject(ResourcePath path) {
        InputStream io = path.stream();
        if (io == null)
            return null;
        try {
            return JANKSON.load(io);
        } catch (IOException | SyntaxError e) {
            e.printStackTrace();
            return null;
        }
    }
}
