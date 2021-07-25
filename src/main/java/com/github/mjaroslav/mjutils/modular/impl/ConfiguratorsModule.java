package com.github.mjaroslav.mjutils.modular.impl;

import javax.annotation.Nonnull;

public class ConfiguratorsModule extends ModularAdapter {
    public ConfiguratorsModule(@Nonnull String name) {
        super(name);
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

    @Override
    public boolean isSubmodule() {
        return false;
    }
}
