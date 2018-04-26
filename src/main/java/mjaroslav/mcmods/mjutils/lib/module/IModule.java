package mjaroslav.mcmods.mjutils.lib.module;

import cpw.mods.fml.common.Loader;

/**
 * Add a {@link ModModule} annotation to connect a module to modification.
 *
 * @author MJaroslav
 */
public interface IModule extends IInit {
    /**
     * Module name, optional. Use 'Proxy' for proxy modules and 'Config' for
     * configuration module, they will be ignored when searching.
     *
     * @return Module name.
     */
    public String getModuleName();

    /**
     * Priority of module loading. If you use an existing level, then this
     * module will shift the rest. If you use an extra-high level, the module
     * will be installed at the end of the list.
     *
     * @return Priority: 0 - first, 1 - second...
     */
    public int getPriority();

    /**
     * Will be checked in {@link Loader#isModLoaded(String)}.
     * 
     * @return Array of modid.
     */
    public String[] modDependencies();

    /**
     * Can load module.
     * 
     * @return Set true if you wont use it.
     */
    public boolean canLoad();
}
