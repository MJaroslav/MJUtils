package io.github.mjaroslav.mjutils.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import io.github.mjaroslav.mjutils.util.object.game.ResourcePath;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * {@link Config} implementation for {@link Jankson} with some helping methods.
 *
 * @see Jankson
 */
@Getter
@Setter
public class Json5Config extends Config {
    /**
     * Key name for config version.
     */
    public static final String VERSION_KEY = "version";

    protected final @NotNull Jankson jankson;
    /**
     * Not deserialized value.
     */
    protected @NotNull JsonObject value = new JsonObject();
    protected @Nullable Object defaultValue;

    /**
     * @see Json5Config#Json5Config(String, Path, String, Object)  Full constructor.
     */
    public Json5Config(@NotNull Path file) {
        this(null, file, null, null);
    }

    /**
     * @see Json5Config#Json5Config(String, Path, String, Object)  Full constructor.
     */
    public Json5Config(@Nullable String modId, @NotNull Path file) {
        this(modId, file, null, null);
    }

    /**
     * @see Json5Config#Json5Config(String, Path, String, Object)  Full constructor.
     */
    public Json5Config(@NotNull Path file, @Nullable String version, @Nullable Object defaultValue) {
        this(null, file, version, defaultValue);
    }

    /**
     * @param defaultValue default value, can be {@link JsonObject}, {@link ResourcePath} and {@link String}
     *                     (or {@link Path}) for {@link Config#resolveDefaultFileResourcePath(String, Path)}.
     *                     Also, can be null if config not be restored to default.
     * @see Config#Config(String, Path, String) Super constructor for another parameters.
     */
    public Json5Config(@Nullable String modId, @NotNull Path file, @Nullable String version, @Nullable Object defaultValue) {
        super(modId, file, version);
        this.defaultValue = defaultValue;
        jankson = buildJankson();
    }

    /**
     * Build Jankson with special (de-) serializers if you need.
     *
     * @return ready Jankson object.
     */
    @NotNull
    protected Jankson buildJankson() {
        return Jankson.builder().build();
    }

    /**
     * Deserialize value to param.
     *
     * @param clazz type for deserialization.
     * @return deserialized value.
     */
    @NotNull
    public <T> T get(@NotNull Class<T> clazz) {
        return jankson.fromJson(value, clazz);
    }

    /**
     * Serialize object to value.
     *
     * @param object object for serialization.
     */
    public void set(@NotNull Object object) {
        value = (JsonObject) jankson.toJson(object);
    }

    @Override
    protected void loadFile() throws Exception {
        val file = getFile();
        if (Files.isRegularFile(file))
            value = jankson.load(file.toFile());
        else restoreDefaultFile();
    }

    @Override
    protected void saveFile() throws IOException {
        Files.createDirectories(file.toAbsolutePath().getParent());
        Files.write(file, value.toJson(JsonGrammar.JANKSON).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void restoreDefaultFile() throws Exception {
        if (defaultValue instanceof Path path)
            defaultValue = Config.resolveDefaultFileResourcePath(getModId(), path);
        if (defaultValue instanceof String string)
            defaultValue = Config.resolveDefaultFileResourcePath(getModId(), Paths.get(string));
        if (defaultValue instanceof ResourcePath path) {
            val file = getFile();
            Files.copy(path.stream(), file, StandardCopyOption.REPLACE_EXISTING);
            value = jankson.load(file.toFile());
        } else if (defaultValue instanceof JsonObject object) value = object.clone();
        else throw new IllegalStateException("Not supported defaultValues format: " + defaultValue);
        saveFile();
    }

    @Override
    protected @Nullable String getLoadedVersion() {
        val value = this.value.get(JsonPrimitive.class, VERSION_KEY);
        return value != null ? value.asString() : null;
    }
}
