package com.github.mjaroslav.mjutils.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import com.github.mjaroslav.mjutils.util.io.UtilsFiles;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Json5Config extends Config {
    protected final @NotNull Jankson jankson;
    @Getter
    @Setter
    protected @NotNull JsonObject value;

    public Json5Config(@NotNull Path file) {
        super("json5", file);
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
    protected void loadFile(@NotNull Path file) {
        try {
            value = jankson.load(file.toFile());
        } catch (IOException | SyntaxError e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void saveFile(@NotNull Path file) {
        try {
            Files.createDirectories(file.toAbsolutePath().getParent());
            Files.write(file, value.toJson(JsonGrammar.JANKSON).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
