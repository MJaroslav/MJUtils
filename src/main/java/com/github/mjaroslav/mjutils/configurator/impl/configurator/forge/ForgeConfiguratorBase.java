package com.github.mjaroslav.mjutils.configurator.impl.configurator.forge;

import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;
import com.github.mjaroslav.mjutils.configurator.impl.configurator.ConfiguratorAdapter;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;
import java.io.File;

public abstract class ForgeConfiguratorBase<T> extends ConfiguratorAdapter<T> {
    public static final String EXT = "cfg";

    protected Configuration configurationInstance;
    protected Configuration defaultConfigurationInstance;

    public ForgeConfiguratorBase(@Nonnull ConfiguratorsLoader loader, @Nonnull String fileName) {
        super(loader, fileName, EXT);
    }

    @Override
    public boolean hasChanges() {
        return super.hasChanges() || getConfigurationInstance().hasChanged();
    }

    @Nonnull
    public Configuration getConfigurationInstance() {
        if (configurationInstance == null)
            configurationInstance = new Configuration(new File(getFile()), getActualVersion(), true);
        return configurationInstance;
    }

    @Nonnull
    @Override
    public String getLocalVersion() {
        String loaded = getConfigurationInstance().getLoadedConfigVersion();
        return loaded != null ? loaded : UNKNOWN_VERSION;
    }

    protected abstract State loadProperties(Configuration instance);

    @Nonnull
    @Override
    public State load() {
        try {
            State result = loadProperties(getConfigurationInstance());
            if (result.isNotCool())
                return State.ERROR;
            if (defaultConfigurationInstance == null)
                defaultConfigurationInstance = getConfigurationInstance();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return State.ERROR;
        }
    }

    @Nonnull
    @Override
    public State save() {
        try {
            if (isReadOnly())
                return State.READONLY;
            getConfigurationInstance().save();
            hasChanges = false;
            return State.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return State.ERROR;
        }
    }
}
