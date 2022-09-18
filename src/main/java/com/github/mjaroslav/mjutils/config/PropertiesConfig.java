package com.github.mjaroslav.mjutils.config;

import lombok.Getter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesConfig extends Config {
    @Getter
    protected @NotNull Path @NotNull [] files;
    @Getter
    protected @NotNull Path file;
    protected final Map<String, String> comments = new HashMap<>();

    public final Properties values = new Properties();

    public PropertiesConfig(@NotNull Path file) {
        super("properties");
        files = new Path[]{file};
        this.file = file;
    }

    public @Nullable String getComment(@NotNull String key) {
        return comments.get(key);
    }

    public void setComment(@NotNull String key, @Nullable String comment) {
        if (comment == null) comments.remove(key);
        else comments.put(key, comment);
    }

    public @NotNull String getString(@NotNull String key) {
        return values.getProperty(key);
    }

    public @NotNull String getString(@NotNull String key, @NotNull String defaultValue) {
        return values.getProperty(key, defaultValue);
    }

    public boolean getBoolean(@NotNull String key) {
        return Boolean.parseBoolean(values.getProperty(key));
    }

    public boolean getBoolean(@NotNull String key, boolean defaultValue) {
        return values.containsKey(key) ? Boolean.parseBoolean(values.getProperty(key)) : defaultValue;
    }

    public int getInt(@NotNull String key) {
        return Integer.parseInt(values.getProperty(key));
    }

    public int getInt(@NotNull String key, int defaultValue) {
        return values.containsKey(key) ? Integer.parseInt(values.getProperty(key)) : defaultValue;
    }

    public long getLong(@NotNull String key) {
        return Long.parseLong(values.getProperty(key));
    }

    public long getLong(@NotNull String key, long defaultValue) {
        return values.containsKey(key) ? Long.parseLong(values.getProperty(key)) : defaultValue;
    }

    public float getFloat(@NotNull String key) {
        return Float.parseFloat(values.getProperty(key));
    }

    public float getFloat(@NotNull String key, float defaultValue) {
        return values.containsKey(key) ? Float.parseFloat(values.getProperty(key)) : defaultValue;
    }

    public double getDouble(@NotNull String key) {
        return Double.parseDouble(values.getProperty(key));
    }

    public double getDouble(@NotNull String key, double defaultValue) {
        return values.containsKey(key) ? Double.parseDouble(values.getProperty(key)) : defaultValue;
    }

    public @NotNull String @NotNull [] getStringArray(@NotNull String key) {
        val raw = values.getProperty(key);
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

    public void setValue(@NotNull String key, @NotNull Object value, @Nullable String comment) {
        if (value instanceof Object[] array) { // TODO: Add support for primitive type arrays
            if (array.length == 0) values.setProperty(key, "");
            else if (array.length == 1) values.setProperty(key, array[0].toString());
            else {
                val builder = new StringBuilder().append("[ ").append(array[0]);
                for (var i = 1; i < array.length; i++) builder.append(" | ").append(array[i]);
                values.setProperty(key, builder.append(" ]").toString());
            }
        } else values.setProperty(key, value.toString());
        comments.put(key, comment);
    }

    public void setValue(@NotNull String key, @NotNull Object value) {
        setValue(key, value, null);
    }

    @Override
    protected void loadFile(@NotNull Path file) {
        loadProperties();
    }

    @Override
    protected void saveFile(@NotNull Path file) {
        saveProperties();
    }

    protected void loadProperties() {
        try {
            val lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            val line = String.join("\n", lines);
            comments.clear();
            StringBuilder comment = new StringBuilder();
            var i = 0;
            while (i < lines.size()) {
                if (lines.get(i).startsWith("#")) comment.append(lines.get(i)).append(System.lineSeparator());
                else if (StringUtils.isNotBlank(lines.get(i))) {
                    val key = lines.get(i).split("=")[0];
                    if (StringUtils.isNotBlank(comment.toString())) comments.put(key, comment.toString().trim());
                    comment = new StringBuilder();
                }
                i++;
            }
            values.clear(); // Resetting
            values.load(new StringReader(line));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void saveProperties() {
        try {
            val builder = new StringBuilder();
            values.forEach((rawKey, value) -> {
                val key = (String) rawKey;
                if (comments.get(key) != null) {
                    for (var comment : comments.get(key).split("\\r?\\n"))
                        if (comment.startsWith("#")) builder.append(comment).append(System.lineSeparator());
                        else builder.append("# ").append(comment).append(System.lineSeparator());
                }
                builder.append(key).append('=').append(value).append(System.lineSeparator());
            });
            // WTF I have 8 lang level but this shit warn anyway
            //noinspection ReadWriteStringCanBeUsed
            Files.write(file, builder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
