package com.github.mjaroslav.mjutils.configurator;

import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import com.github.mjaroslav.mjutils.util.io.UtilsFiles;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

@Getter
@Setter
public class PropertiesConfigurator extends Configurator {
    protected @NotNull ResourcePath defaultPath;
    protected @Nullable String comments;

    protected final @NotNull Properties properties = new Properties();
    protected final @NotNull Properties defaultProperties = new Properties();

    public PropertiesConfigurator(@NotNull String fileName, @NotNull ResourcePath defaultPath,
                                  @Nullable String comments) {
        super(fileName, "properties");
        this.defaultPath = defaultPath;
        this.comments = comments;
        loadDefaultProperties();
    }

    public @Nullable String getLocalVersion() {
        return properties.getProperty("config_version", null);
    }

    public @Nullable String getActualVersion() {
        return defaultProperties.getProperty("config_version", null);
    }

    @Override
    public void load() {
        val path = getFile();
        if (!Files.isRegularFile(path))
            restoreDefault();
        else if (UtilsFiles.createDirectories(path.getParent()) != null)
            try {
                properties.load(Files.newBufferedReader(path, StandardCharsets.UTF_8));
                if (!StringUtils.equals(getLocalVersion(), getActualVersion())) {
                    UtilsFiles.move(path, path + ".old", StandardCopyOption.REPLACE_EXISTING);
                    restoreDefault();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void save() {
        try {
            val path = getFile();
            if (UtilsFiles.createDirectories(path.getParent()) != null)
                properties.store(Files.newBufferedWriter(path, StandardCharsets.UTF_8), comments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void loadDefaultProperties() {
        val reader = defaultPath.bufferedReader(StandardCharsets.UTF_8);
        if (reader != null)
            try {
                defaultProperties.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void restoreDefault() {
        properties.clear();
        val path = getFile();
        val reader = defaultPath.bufferedReader(StandardCharsets.UTF_8);
        if (reader != null)
            try {
                IOUtils.copy(reader, Files.newBufferedWriter(path, StandardCharsets.UTF_8));
                load();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
