package com.github.mjaroslav.mjutils.configurator.impl.loader;

import com.github.mjaroslav.mjutils.configurator.Configurator;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorEvents;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;
import cpw.mods.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

public class SingleConfiguratorLoader<T extends Configurator<?>> implements ConfiguratorsLoader {
    @Nonnull
    protected final String MOD_ID;

    @Nonnull
    protected T config;
    protected boolean enableEvents;

    public SingleConfiguratorLoader(@Nonnull String modId, @Nonnull T configuratorForWrap, boolean enableEvents) {
        config = configuratorForWrap;
        MOD_ID = modId;
        this.enableEvents = enableEvents;
    }

    @Nonnull
    @Override
    public String getModId() {
        return MOD_ID;
    }

    @Nonnull
    public T getConfig() {
        return config;
    }

    @Nonnull
    public T data() {
        return config;
    }

    @Override
    public void addConfigurators(Configurator<?>... configurators) {
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
        if (!config.isReadOnly() && config.hasChanges() && config.save().isNotCool() && config.canCrashOnError())
            throw new RuntimeException("Error on configuration saving! See console for more info");
    }

    @Override
    public void restoreToDefault() {
        if (!config.isReadOnly() && config.load().isNotCool() && config.canCrashOnError())
            throw new RuntimeException("Error on configuration restoring! See console for more info");
        save();
    }

    @Override
    public void forceSync() {
        Configurator.State state = config.sync();
        if (state.isNotCool() && config.canCrashOnError())
            throw new RuntimeException("Error on configuration syncing! See console for more info");
        // TODO: Write asking for world/game restart.
        if (enableEvents)
            if (ConfiguratorEvents.onConfiguratorChangedEventPost(config, false, false) != Event.Result.DENY)
                ConfiguratorEvents.postConfiguratorChangedEventPost(config, false, false);
    }

    @Override
    public T getConfigurator(String name) {
        return config;
    }

    public static <T extends Configurator<?>> SingleConfiguratorLoader<T> wrap(String modId, T configurator, boolean enableEvents) {
        return new SingleConfiguratorLoader<>(modId, configurator, enableEvents);
    }
}
