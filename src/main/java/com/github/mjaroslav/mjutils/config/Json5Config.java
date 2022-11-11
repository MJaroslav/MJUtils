package com.github.mjaroslav.mjutils.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.api.SyntaxError;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Json5Config extends Config {
    public static final String VERSION_KEY = "version";

    protected final @NotNull Jankson jankson;
    @Getter
    @Setter
    protected @NotNull JsonObject value = new JsonObject();
    @Getter
    @Setter
    protected @Nullable Object defaultValue;

    public Json5Config(@NotNull Path file) {
        this(file, null, null);
    }

    public Json5Config(@NotNull Path file, @Nullable String version, @Nullable Object defaultValue) {
        super(file, version);
        this.defaultValue = defaultValue;
        jankson = buildJankson();
    }

    // For adding custom (de-)serializers
    @NotNull
    protected Jankson buildJankson() {
        return Jankson.builder().build();
    }

    @NotNull
    public <T> T get(@NotNull Class<T> clazz) {
        return jankson.fromJson(value, clazz);
    }

    public void set(@NotNull Object object) {
        value = (JsonObject) jankson.toJson(object);
    }

    @Override
    protected void loadFile() throws IOException {
        try {
            value = jankson.load(file.toFile());
        } catch (SyntaxError e) {
            // TODO: Wrap it normally
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void saveFile() throws IOException {
        Files.createDirectories(file.toAbsolutePath().getParent());
        Files.write(file, value.toJson(JsonGrammar.JANKSON).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void setDefault() throws IOException {
        if (defaultValue instanceof ResourcePath path) {
            Files.copy(path.stream(), getFile(), StandardCopyOption.REPLACE_EXISTING);
            loadFile();
        } else if (defaultValue instanceof JsonObject object)
            value = object.clone();
        else throw new IllegalStateException("Not supported defaultValues format: " + defaultValue);
        saveFile();
    }

    @Override
    protected @Nullable String getLoadedVersion() {
        val value = this.value.get(JsonPrimitive.class, VERSION_KEY);
        return value != null ? value.asString() : null;
    }
}
