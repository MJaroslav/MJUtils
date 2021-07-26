package com.github.mjaroslav.mjutils.modular.impl;

import com.github.mjaroslav.mjutils.modular.ModuleLoader;

import javax.annotation.Nonnull;

public abstract class ConfiguratorsModule extends ModularAdapter {
    public ConfiguratorsModule(@Nonnull ModuleLoader loader, @Nonnull String name) {
        super(loader, name);
    }

    @Nonnull
    @Override
    public String getName() {
        return CONFIGURATOR_NAME_PREFIX + NAME;
    }

    @Override
    public int getPriority() {
        return CONFIGURATORS_PRIORITY;
    }
}
