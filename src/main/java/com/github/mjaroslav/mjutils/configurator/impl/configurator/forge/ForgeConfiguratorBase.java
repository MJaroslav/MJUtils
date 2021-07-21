package com.github.mjaroslav.mjutils.configurator.impl.configurator.forge;

import com.github.mjaroslav.mjutils.configurator.impl.configurator.ConfiguratorAdapter;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;
import java.io.File;

public abstract class ForgeConfiguratorBase extends ConfiguratorAdapter {
    public static final String EXT = "cfg";

    protected Configuration instance;
    protected Configuration defaultInstance;
    protected boolean hasChanges;

    public ForgeConfiguratorBase(@Nonnull String modId, @Nonnull String fileName) {
        super(modId, fileName, EXT);
    }

    @Override
    public boolean hasChanges() {
        return hasChanges || getInstance().hasChanged();
    }

    @Nonnull
    public Configuration getInstance() {
        if (instance == null)
            instance = new Configuration(new File(getFile()), getActualVersion(), true);
        return instance;
    }

    @Nonnull
    @Override
    public String getLocalVersion() {
        String loaded = getInstance().getLoadedConfigVersion();
        return loaded != null ? loaded : UNKNOWN_VERSION;
    }

    protected abstract State loadProperties(Configuration instance);

    @Nonnull
    @Override
    public State load() {
        try {
            State result = loadProperties(getInstance());
            if (result.isNotCool())
                return State.ERROR;
            if (defaultInstance == null)
                defaultInstance = getInstance();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return State.ERROR;
        }
    }

    @Nonnull
    @Override
    public State restoreDefault() {
        if (isReadOnly())
            return State.READONLY;
        instance = defaultInstance;
        hasChanges = true;
        return State.OK;
    }

    @Nonnull
    @Override
    public State save() {
        try {
            if (isReadOnly())
                return State.READONLY;
            getInstance().save();
            hasChanges = false;
            return State.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return State.ERROR;
        }
    }
}
