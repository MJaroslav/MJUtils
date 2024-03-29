package io.github.mjaroslav.mjutils.config;

import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import io.github.mjaroslav.mjutils.util.object.game.ResourcePath;
import lombok.Getter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * {@link Config} implementation for {@link Properties} with some helping methods.
 *
 * @see Properties
 */
@Getter
public class PropertiesConfig extends Config {
    /**
     * Property name for config version.
     */
    public static final String VERSION_KEY = "version";

    protected final Map<String, String> comments = new HashMap<>();
    protected final Map<String, String> values = new LinkedHashMap<>();
    protected @Nullable Object defaultValues;

    /**
     * @see PropertiesConfig#PropertiesConfig(String, Path, String, Object)  Full constructor.
     */
    public PropertiesConfig(@NotNull Path file) {
        this(null, file, null, null);
    }

    /**
     * @see PropertiesConfig#PropertiesConfig(String, Path, String, Object)  Full constructor.
     */
    public PropertiesConfig(@Nullable String modId, @NotNull Path file) {
        this(modId, file, null, null);
    }


    /**
     * @see PropertiesConfig#PropertiesConfig(String, Path, String, Object)  Full constructor.
     */
    public PropertiesConfig(@NotNull Path file, @Nullable String version, @Nullable Object defaultValues) {
        this(null, file, version, defaultValues);
    }

    /**
     * @param defaultValues default values, can be {@link Properties}, {@link ResourcePath} and {@link String}
     *                      (or {@link Path}) for {@link Config#resolveDefaultFileResourcePath(String, Path)}.
     *                      Also, can be null if config not be restored to default.
     * @see Config#Config(String, Path, String) Super constructor for another parameters.
     */
    public PropertiesConfig(@Nullable String modId, @NotNull Path file, @Nullable String version, @Nullable Object defaultValues) {
        super(StringUtils.isEmpty(modId) ? UtilsMods.getActiveModId() : modId, file, version);
        this.defaultValues = defaultValues;
    }

    public @Nullable String getComment() {
        return getComment(VERSION_KEY);
    }

    public void setComment(@Nullable String comment) {
        setComment(VERSION_KEY, comment);
    }

    public @Nullable String getComment(@NotNull String key) {
        return comments.get(key);
    }

    public void setComment(@NotNull String key, @Nullable String comment) {
        if (comment == null) comments.remove(key);
        else comments.put(key, comment);
    }

    public @NotNull String get(@NotNull String key) {
        return values.get(key);
    }

    public @NotNull String get(@NotNull String key, @NotNull String defaultValue) {
        return values.getOrDefault(key, defaultValue);
    }

    public boolean getBoolean(@NotNull String key) {
        return Boolean.parseBoolean(get(key));
    }

    public boolean getBoolean(@NotNull String key, boolean defaultValue) {
        return values.containsKey(key) ? Boolean.parseBoolean(values.get(key)) : defaultValue;
    }

    public int getInt(@NotNull String key) {
        return Integer.parseInt(values.get(key));
    }

    public int getInt(@NotNull String key, int defaultValue) {
        return values.containsKey(key) ? Integer.parseInt(values.get(key)) : defaultValue;
    }

    public long getLong(@NotNull String key) {
        return Long.parseLong(values.get(key));
    }

    public long getLong(@NotNull String key, long defaultValue) {
        return values.containsKey(key) ? Long.parseLong(values.get(key)) : defaultValue;
    }

    public float getFloat(@NotNull String key) {
        return Float.parseFloat(values.get(key));
    }

    public float getFloat(@NotNull String key, float defaultValue) {
        return values.containsKey(key) ? Float.parseFloat(values.get(key)) : defaultValue;
    }

    public double getDouble(@NotNull String key) {
        return Double.parseDouble(values.get(key));
    }

    public double getDouble(@NotNull String key, double defaultValue) {
        return values.containsKey(key) ? Double.parseDouble(values.get(key)) : defaultValue;
    }

    public @NotNull String @NotNull [] getStringArray(@NotNull String key) {
        val raw = values.get(key);
        if (raw.startsWith("[") && raw.endsWith("]")) {
            val result = raw.substring(1, raw.length() - 1).split("\\|");
            for (var i = 0; i < result.length; i++) result[i] = result[i].trim();
            return result;
        } else return new String[]{raw.trim()};
    }

    public @NotNull String @NotNull [] getStringArray(@NotNull String key, @NotNull String @NotNull ... defaultValue) {
        return values.containsKey(key) ? getStringArray(key) : defaultValue;
    }

    public boolean @NotNull [] getBooleanArray(@NotNull String key) {
        val raw = getStringArray(key);
        val result = new boolean[raw.length];
        for (var i = 0; i < raw.length; i++) result[i] = Boolean.parseBoolean(raw[i]);
        return result;
    }

    public boolean @NotNull [] getBooleanArray(@NotNull String key, boolean @NotNull ... defaultValue) {
        return values.containsKey(key) ? getBooleanArray(key) : defaultValue;
    }

    public int @NotNull [] getIntArray(@NotNull String key) {
        val raw = getStringArray(key);
        val result = new int[raw.length];
        for (var i = 0; i < raw.length; i++) result[i] = Integer.parseInt(raw[i]);
        return result;
    }

    public int @NotNull [] getIntArray(@NotNull String key, int @NotNull ... defaultValue) {
        return values.containsKey(key) ? getIntArray(key) : defaultValue;
    }

    public long @NotNull [] getLongArray(@NotNull String key) {
        val raw = getStringArray(key);
        val result = new long[raw.length];
        for (var i = 0; i < raw.length; i++) result[i] = Long.parseLong(raw[i]);
        return result;
    }

    public long @NotNull [] getLongArray(@NotNull String key, long @NotNull ... defaultValue) {
        return values.containsKey(key) ? getLongArray(key) : defaultValue;
    }

    public float @NotNull [] getFloatArray(@NotNull String key) {
        val raw = getStringArray(key);
        val result = new float[raw.length];
        for (var i = 0; i < raw.length; i++) result[i] = Float.parseFloat(raw[i]);
        return result;
    }

    public float @NotNull [] getFloatArray(@NotNull String key, float @NotNull ... defaultValue) {
        return values.containsKey(key) ? getFloatArray(key) : defaultValue;
    }


    public double @NotNull [] getDoubleArray(@NotNull String key) {
        val raw = getStringArray(key);
        val result = new double[raw.length];
        for (var i = 0; i < raw.length; i++) result[i] = Double.parseDouble(raw[i]);
        return result;
    }

    public double @NotNull [] getDoubleArray(@NotNull String key, double @NotNull ... defaultValue) {
        return values.containsKey(key) ? getDoubleArray(key) : defaultValue;
    }

    /**
     * Primitive arrays not supported, use non-primitive instead.
     */
    public void setValue(@NotNull String key, @NotNull Object value, @Nullable String comment) {
        if (value instanceof Object[] array) { // TODO: Add support for primitive type arrays
            if (array.length == 0) values.put(key, "");
            else if (array.length == 1) values.put(key, array[0].toString());
            else {
                val builder = new StringBuilder().append("[ ").append(array[0]);
                for (var i = 1; i < array.length; i++) builder.append(" | ").append(array[i]);
                values.put(key, builder.append(" ]").toString());
            }
        } else values.put(key, value.toString());
        comments.put(key, comment);
    }

    /**
     * @see PropertiesConfig#setValue(String, Object, String) Full method.
     */
    public void setValue(@NotNull String key, @NotNull Object value) {
        setValue(key, value, null);
    }

    @Override
    protected void loadFile() throws IOException {
        if (Files.isRegularFile(getFile())) parseFile();
        else restoreDefaultFile();
    }

    private void parseFile() throws IOException {
        val lines = Files.readAllLines(getFile(), StandardCharsets.UTF_8);
        val line = String.join(System.lineSeparator(), lines);
        comments.clear();
        values.clear();
        var comment = new StringBuilder();
        var i = 0;
        while (i < lines.size()) {
            val current = lines.get(i);
            if (current.startsWith("#") || current.startsWith("!"))
                comment.append(current.replaceFirst("^#", "").replaceFirst("^ ", "")).append(System.lineSeparator());
            else if (StringUtils.isNotBlank(current)) {
                var precedingBackslash = false;
                var separatorPosition = 0;
                char character;
                var hasSeparator = false;
                while (separatorPosition < current.length()) {
                    character = current.charAt(separatorPosition);
                    if ((character == '=' || character == ':') && !precedingBackslash) {
                        hasSeparator = true;
                        break;
                    }
                    if (character == '\\') precedingBackslash = !precedingBackslash;
                    else precedingBackslash = false;
                    separatorPosition++;
                }
                if (hasSeparator) {
                    val key = current.substring(0, separatorPosition);
                    val value = current.substring(separatorPosition + 1);
                    if (StringUtils.isNotBlank(comment.toString())) {
                        comments.put(key, comment.toString().trim());
                        comment = new StringBuilder();
                    }
                    values.put(key, value);
                } // Ignore other lines
            }
            i++;
        }
    }

    @Override
    protected void saveFile() throws IOException {
        val builder = new StringBuilder();
        values.entrySet().stream().forEach(entry -> {
            val key = (String) entry.getKey();
            if (comments.get(key) != null)
                // New lines written by other devs, and they usually use just \n
                for (var comment : comments.get(key).split("(\\r?\\n)|(" + System.lineSeparator() + ")"))
                    builder.append("# ").append(comment).append(System.lineSeparator());
            builder.append(key).append('=').append(entry.getValue()).append(System.lineSeparator());
        });
        Files.write(getFile(), builder.toString().getBytes(StandardCharsets.UTF_8));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void restoreDefaultFile() throws IOException {
        if (defaultValues instanceof Path path)
            defaultValues = Config.resolveDefaultFileResourcePath(getModId(), path);
        if (defaultValues instanceof String string)
            defaultValues = Config.resolveDefaultFileResourcePath(getModId(), Paths.get(string));
        if (defaultValues == null) {
            val map = new HashMap<String, String>();
            map.put(VERSION_KEY, getVersion());
            defaultValues = map;
        }
        if (defaultValues instanceof ResourcePath path) {
            Files.copy(path.stream(), getFile(), StandardCopyOption.REPLACE_EXISTING);
            parseFile();
        } else if (defaultValues instanceof Map<?, ?> map) {
            values.clear();
            values.putAll((Map<String, String>) map);
        } else throw new IllegalStateException("Unsupported defaultValues format: " + defaultValues);
    }

    @Override
    protected @Nullable String getLoadedVersion() {
        return values.get(VERSION_KEY);
    }
}
