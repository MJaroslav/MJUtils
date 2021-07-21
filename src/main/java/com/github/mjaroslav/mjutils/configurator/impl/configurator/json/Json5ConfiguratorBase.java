package com.github.mjaroslav.mjutils.configurator.impl.configurator.json;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import com.github.mjaroslav.mjutils.configurator.impl.configurator.ConfiguratorAdapter;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public abstract class Json5ConfiguratorBase extends ConfiguratorAdapter {
    public static final String VERSION_KEY = "version";
    public static final String EXT = "json5";
    public static final Jankson JANKSON = Jankson.builder().build();

    protected JsonObject jsonInstance;
    protected JsonObject defaultJsonInstance;
    protected boolean hasChanges = false;

    public Json5ConfiguratorBase(@Nonnull String modId, @Nonnull String fileName) {
        super(modId, fileName, EXT);
    }

    @Override
    public boolean hasChanges() {
        return hasChanges;
    }

    @Nonnull
    protected abstract JsonObject createDefaultJsonInstance();

    @Nonnull
    public JsonObject getDefaultJsonInstance() {
        if (defaultJsonInstance == null)
            defaultJsonInstance = createDefaultJsonInstance();
        return defaultJsonInstance;
    }

    @Nullable
    public JsonObject getJsonInstance() {
        return jsonInstance;
    }

    @Nonnull
    @Override
    public String getActualVersion() {
        String loaded = getDefaultJsonInstance().get(String.class, VERSION_KEY);
        return loaded != null ? loaded : UNKNOWN_VERSION;
    }

    @Nonnull
    @Override
    public String getLocalVersion() {
        if (jsonInstance == null)
            return UNKNOWN_VERSION;
        String loaded = jsonInstance.get(String.class, VERSION_KEY);
        return loaded != null ? loaded : UNKNOWN_VERSION;
    }

    @Nonnull
    @Override
    public State load() {
        Path path = Paths.get(getFile());
        if (!Files.isRegularFile(path))
            return restoreDefault() == State.OK && save() == State.OK ? State.OK : State.ERROR;
        else {
            try {
                jsonInstance = JANKSON.load(Files.newInputStream(path));
                if (getLocalVersion().equals(getActualVersion()))
                    return State.OK;
                else {
                    Path pathForRename = Paths.get(String.format("%s.%s", getFile(), OLD_VERSION_FILE_EXT));
                    Files.move(path, pathForRename, StandardCopyOption.REPLACE_EXISTING);
                    return restoreDefault() == State.OK && save() == State.OK ? State.OK : State.ERROR;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return State.ERROR;
            }
        }
    }

    @Nonnull
    @Override
    public State restoreDefault() {
        try {
            jsonInstance = getDefaultJsonInstance();
            return State.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return State.ERROR;
        }
    }

    @Nonnull
    @Override
    public State save() {
        if (jsonInstance == null)
            return State.ERROR;
        String json = jsonInstance.toJson(true, true);
        try {
            Files.write(Paths.get(getFile()), json.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
            hasChanges = false;
            return State.OK;
        } catch (IOException e) {
            e.printStackTrace();
            return State.ERROR;
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