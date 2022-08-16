package com.github.mjaroslav.mjutils.configurator;

import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.Collectors;

public class PropertiesConfigurator extends Configurator {
    @Nonnull
    protected final ResourcePath defaultPath;

    @Nonnull
    @Getter
    protected Properties properties = new Properties();

    @Nonnull
    protected Properties defaultProperties = new Properties();

    public PropertiesConfigurator(@Nonnull String fileName, @Nonnull ResourcePath defaultPath) {
        super(fileName, "properties");
        this.defaultPath = defaultPath;
        loadDefaultProperties();
    }

    @Nullable
    public String getLocalVersion() {
        return properties.getProperty("config_version", null);
    }

    @Nullable
    public String getActualVersion() {
        return defaultProperties.getProperty("config_version", null);
    }

    public void setProperties(@Nonnull Properties properties) {
        this.properties = properties;
        sync();
    }

    @Override
    public void load() {
        Path path = Paths.get(getFile());
        if (!Files.isRegularFile(path))
            restoreDefault();
        else {
            try {
                properties.load(Files.newInputStream(path));
                if (!StringUtils.equals(getLocalVersion(), getActualVersion())) {
                    Path pathForRename = Paths.get(getFile() + ".old");
                    Files.move(path, pathForRename, StandardCopyOption.REPLACE_EXISTING);
                    restoreDefault();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sync();
    }

    @Override
    public void save() {
        try {
            Path path = Paths.get(getFile());
            Files.createDirectories(path.getParent());
            properties.store(Files.newBufferedWriter(path), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sync() {
    }

    private void loadDefaultProperties() {
        defaultProperties.clear();
        InputStream io = defaultPath.stream();
        if (io != null) {
            try {
                defaultProperties.load(io);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void restoreDefault() {
        properties = defaultProperties;
        sync();
        try {
            Files.createDirectories(Paths.get(getFile()).getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = "";
        BufferedReader reader = defaultPath.bufferedReader();
        if (reader != null)
            result = reader.lines().collect(Collectors.joining("\n"));
        try {
            Files.write(Paths.get(getFile()), Collections.singleton(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
