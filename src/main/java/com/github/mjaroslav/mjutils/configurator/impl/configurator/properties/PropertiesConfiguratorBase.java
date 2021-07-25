package com.github.mjaroslav.mjutils.configurator.impl.configurator.properties;

import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;
import com.github.mjaroslav.mjutils.configurator.impl.configurator.ConfiguratorAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.*;
import java.util.Properties;

public abstract class PropertiesConfiguratorBase<T> extends ConfiguratorAdapter<T> {
    public static final String FILE_EXT = "properties";
    public static final String VERSION_KEY = "version";

    protected Properties propertiesInstance;
    protected Properties propertiesDefaultInstance;

    public PropertiesConfiguratorBase(@Nonnull ConfiguratorsLoader loader, @Nonnull String fileName) {
        super(loader, fileName, FILE_EXT);
        propertiesInstance = new Properties();
    }

    @Nonnull
    protected abstract Properties createDefaultPropertiesInstance();

    @Nullable
    public Properties getPropertiesInstance() {
        return propertiesInstance;
    }

    @Nonnull
    public Properties getDefaultPropertiesInstance() {
        if (propertiesDefaultInstance == null)
            propertiesDefaultInstance = createDefaultPropertiesInstance();
        return propertiesDefaultInstance;
    }

    @Nonnull
    @Override
    public String getLocalVersion() {
        return propertiesInstance.getProperty(VERSION_KEY, UNKNOWN_VERSION);
    }

    @Nonnull
    @Override
    public State load() {
        Path path = Paths.get(getFile());
        if (!Files.isRegularFile(path))
            return restoreDefault() == State.OK && save() == State.OK ? State.OK : State.ERROR;
        else {
            try {
                propertiesInstance.load(Files.newInputStream(path));
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
    public State save() {
        try {
            Path path = Paths.get(getFile());
            Files.createFile(path);
            propertiesInstance.store(Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING), null);
            hasChanges = false;
            return State.OK;
        } catch (IOException e) {
            e.printStackTrace();
            return State.ERROR;
        }
    }
}
