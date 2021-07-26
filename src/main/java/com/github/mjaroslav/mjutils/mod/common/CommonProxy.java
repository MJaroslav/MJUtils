package com.github.mjaroslav.mjutils.mod.common;

import com.github.mjaroslav.mjutils.mod.MJUtils;
import com.github.mjaroslav.mjutils.modular.ModuleLoader;
import com.github.mjaroslav.mjutils.modular.impl.ProxyAdapter;

import javax.annotation.Nonnull;

public abstract class CommonProxy extends ProxyAdapter {
    @Nonnull
    @Override
    public ModuleLoader getLoader() {
        return MJUtils.loader;
    }

    @Override
    public boolean isGuiHandlerUsed() {
        return false;
    }
}
