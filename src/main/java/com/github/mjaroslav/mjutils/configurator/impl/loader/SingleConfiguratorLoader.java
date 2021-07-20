package com.github.mjaroslav.mjutils.configurator.impl.loader;

import com.github.mjaroslav.mjutils.configurator.Configurator;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;

import javax.annotation.Nonnull;

public class SingleConfiguratorLoader<T extends Configurator> implements ConfiguratorsLoader {
    @Nonnull
    public T config;

    public SingleConfiguratorLoader(@Nonnull T configuratorForWrap) {
        config = configuratorForWrap;
    }

    @Override
    public boolean addConfigurators(Configurator... configurators) {
        return false;
    }

    @Override
    public void load() {
        Configurator.State state = config.load();
        if (state.isNotCool() && config.canCrashOnError())
            throw new RuntimeException("Error on configuration loading! See console for more info");
        if (state == Configurator.State.OK)
            forceSync();
    }

    public void reload() {
        save();
        load();
    }

    @Override
    public void save() {
        if (config.hasChanges() && config.save().isNotCool() && config.canCrashOnError())
            throw new RuntimeException("Error on configuration saving! See console for more info");
    }

    @Override
    public void restoreToDefault() {
        if (config.load().isNotCool() && config.canCrashOnError())
            throw new RuntimeException("Error on configuration restoring! See console for more info");
    }

    @Override
    public void forceSync() {
        Configurator.State state = config.sync();
        if (state.isNotCool() && config.canCrashOnError())
            throw new RuntimeException("Error on configuration syncing! See console for more info");
        // TODO: Write asking for world/game restart.
    }

    @Override
    public Configurator getConfigurator(String name) {
        return config;
    }

    public static <T extends Configurator> SingleConfiguratorLoader<T> wrap(T configurator) {
        return new SingleConfiguratorLoader<>(configurator);
    }
}
