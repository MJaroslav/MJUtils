package com.github.mjaroslav.mjutils.configurator.impl.loader;

import com.github.mjaroslav.mjutils.configurator.Configurator;
import com.github.mjaroslav.mjutils.configurator.ConfiguratorsLoader;
import com.github.mjaroslav.mjutils.configurator.impl.configurator.properties.ManualPropertiesConfigurator;
import com.github.mjaroslav.mjutils.configurator.impl.configurator.properties.PropertiesConfiguratorBase;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class HookConfiguratorLoader implements ConfiguratorsLoader {
    @Nonnull
    protected final String MOD_ID;
    @Nonnull
    protected final ManualPropertiesConfigurator CONFIGURATOR;

    protected Set<String> enabled;

    public HookConfiguratorLoader(@Nonnull String modId, @Nonnull String fileName, @Nonnull ResourcePath defaultPath) {
        MOD_ID = modId;
        CONFIGURATOR = new ManualPropertiesConfigurator(this, fileName, defaultPath);
        CONFIGURATOR.makeCrashOnError();
    }

    @Nonnull
    @Override
    public String getModId() {
        return MOD_ID;
    }

    @Override
    public void addConfigurators(Configurator<?>... configurators) {
    }

    @Override
    public void load() {
        Configurator.State state = CONFIGURATOR.load();
        if (state.isNotCool() && CONFIGURATOR.canCrashOnError())
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
        if (!CONFIGURATOR.isReadOnly() && CONFIGURATOR.hasChanges() && CONFIGURATOR.save().isNotCool() && CONFIGURATOR.canCrashOnError())
            throw new RuntimeException("Error on configuration saving! See console for more info");
    }

    @Override
    public void restoreToDefault() {
        if (!CONFIGURATOR.isReadOnly() && CONFIGURATOR.load().isNotCool() && CONFIGURATOR.canCrashOnError())
            throw new RuntimeException("Error on configuration restoring! See console for more info");
        save();
    }

    @Override
    public void forceSync() {
        Properties instance = CONFIGURATOR.getInstance();
        if (instance != null)
            enabled = CONFIGURATOR.getInstance().entrySet().stream().filter(entry -> !entry.getKey().equals(PropertiesConfiguratorBase.VERSION_KEY))
                    .filter(entry -> Boolean.parseBoolean((String) entry.getValue())).map(entry -> (String) entry.getKey()).collect(Collectors.toSet());
        Configurator.State state = CONFIGURATOR.sync();
        if (state.isNotCool() && CONFIGURATOR.canCrashOnError())
            throw new RuntimeException("Error on configuration syncing! See console for more info");
    }

    @Override
    public ManualPropertiesConfigurator getConfigurator(String name) {
        return CONFIGURATOR;
    }

    public Set<String> getEnabledHooks() {
        if (enabled == null)
            return Collections.emptySet();
        return enabled;
    }

    public boolean isHookEnabled(String hook) {
        if (enabled == null)
            return false;
        return enabled.contains(hook);
    }
}
