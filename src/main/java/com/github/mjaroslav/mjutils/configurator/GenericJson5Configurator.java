package com.github.mjaroslav.mjutils.configurator;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.api.SyntaxError;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import com.github.mjaroslav.mjutils.util.io.UtilsFiles;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Getter
@Setter
public class GenericJson5Configurator<T> extends Configurator {
    public static final Jankson JANKSON = Jankson.builder().build();

    protected final @NotNull Class<T> typeClass;

    protected @NotNull JsonObject jsonInstance;
    protected @NotNull JsonObject defaultJsonInstance;

    protected final @NotNull ResourcePath defaultPath;

    public GenericJson5Configurator(@Nonnull String fileName, @NotNull Class<T> typeClass, @NotNull ResourcePath defaultPath) {
        super(fileName, "json5");
        this.defaultPath = defaultPath;
        defaultJsonInstance = loadJsonObject(defaultPath);
        this.typeClass = typeClass;
    }

    public @NotNull T getValue() {
        return JANKSON.fromJson(jsonInstance, typeClass);
    }

    public void setValue(@NotNull T value) {
        // TODO: fix comments
        val version = getLocalVersion();
        jsonInstance = (JsonObject) JANKSON.toJson(value);
        if (version != null)
            jsonInstance.put("config_version", JsonPrimitive.of(version));
    }

    public @Nullable String getLocalVersion() {
        return jsonInstance.get(String.class, "config_version");
    }

    public @Nullable String getActualVersion() {
        return defaultJsonInstance.get(String.class, "config_version");
    }

    @Override
    public void load() {
        val path = getFile();
        if (!Files.isRegularFile(path))
            restoreDefault();
        else if (UtilsFiles.createDirectories(path.getParent()) != null)
            try {
                jsonInstance = JANKSON.load(Files.newInputStream(path));
                if (!StringUtils.equals(getLocalVersion(), getActualVersion())) {
                    UtilsFiles.move(path, path + ".old");
                    restoreDefault();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void save() {
        val path = getFile();
        if (UtilsFiles.createDirectories(path.getParent()) != null)
            try {
                val json = jsonInstance.toJson(true, true);
                Files.write(path, json.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void restoreDefault() {
        jsonInstance = loadJsonObject(defaultPath);
        val path = getFile();
        if (UtilsFiles.createDirectories(path.getParent()) != null) {
            val reader = defaultPath.reader(StandardCharsets.UTF_8);
            if (reader != null) {
                try {
                    IOUtils.copy(reader, Files.newBufferedWriter(path, StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected @NotNull JsonObject loadJsonObject(@NotNull ResourcePath path) {
        val io = path.stream();
        if (io == null)
            return new JsonObject();
        try {
            return JANKSON.load(io);
        } catch (IOException | SyntaxError e) {
            e.printStackTrace();
            return new JsonObject();
        }
    }
}
