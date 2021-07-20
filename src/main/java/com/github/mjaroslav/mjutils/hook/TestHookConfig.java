package com.github.mjaroslav.mjutils.hook;

import com.github.mjaroslav.mjutils.configurator.impl.configurator.json.GenericJson5Configurator;
import com.github.mjaroslav.mjutils.configurator.impl.loader.SingleConfiguratorLoader;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;

public class TestHookConfig {
    public static SingleConfiguratorLoader<GenericJson5Configurator<Config>> loader
            = new SingleConfiguratorLoader<>(new GenericJson5Configurator<>(ModInfo.MOD_ID, "mjutils_hooks",
            new Config(), null));

    public static class Config {

    }
}
