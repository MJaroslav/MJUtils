package mjaroslav.mcmods.mjutils.lib.module;

import cpw.mods.fml.common.Loader;

/**
 * Add a {@link Module} annotation to connect a module to modification.
 *
 * @author MJaroslav
 */
public interface Modular extends Initializator {
    /**
     * Module name, optional.
     *
     * @return Module name.
     */
    String getModuleName();

    /**
     * Priority of module loading. If you use an existing level, then this
     * module will shift the rest. If you use an extra-high level, the module
     * will be installed at the end of the list.
     *
     * @return Priority: 0 - first, 1 - second...
     */
    int getPriority();

    /**
     * Will be checked in {@link Loader#isModLoaded(String)}.
     * 
     * @return Array of modid.
     */
    String[] modDependencies();

    /**
     * Can load module.
     * 
     * @return Set true if you wont use it.
     */
    boolean canLoad();
}
