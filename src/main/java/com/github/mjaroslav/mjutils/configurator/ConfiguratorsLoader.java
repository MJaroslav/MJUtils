package com.github.mjaroslav.mjutils.configurator;

import javax.annotation.Nullable;

public interface ConfiguratorsLoader {
    /**
     * Add configurators to loading list.
     *
     * @param configurators New configurators array.
     */
    void addConfigurators(Configurator... configurators);

    /**
     * Call {@link Configurator#load()} for each configurator.
     * <br><br>
     * In custom realization, you should crash game if {@link Configurator#canCrashOnError()} true and its return {@link Configurator.State#ERROR}.
     */
    void load();

    /**
     * Call {@link Configurator#save()} for each configurator.
     * <br><br>
     * In custom realization, you should crash game if {@link Configurator#canCrashOnError()} true and its return {@link Configurator.State#ERROR}.
     */
    void save();

    /**
     * Call {@link Configurator#restoreDefault()} for each configurator.
     * <br><br>
     * In custom realization, you should crash game if {@link Configurator#canCrashOnError()} true and its return {@link Configurator.State#ERROR}.
     */
    void restoreToDefault();

    /**
     * Call {@link Configurator#sync()} for each configurator.
     * <br><br>
     * In custom realization, you should crash game if {@link Configurator#canCrashOnError()} true and its return {@link Configurator.State#ERROR}.
     */
    void forceSync();

    /**
     * Try get configurator by name.
     *
     * @param name Required configurator name.
     *
     * @return Configurator with same name or null if no one found.
     */
    @Nullable
    Configurator getConfigurator(String name);
}
