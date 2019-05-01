package mjaroslav.mcmods.mjutils.module;

/**
 * Use with {@link Module} and {@link ModuleSystem}
 * to automatically perform initialization.
 *
 * @see Module
 * @see ModuleSystem
 */
public interface Modular extends Initializator {
    /**
     * Module name.
     *
     * @return Simple class name as default value;
     */
    default String name() {
        return this.getClass().getSimpleName();
    }

    /**
     * Priority (ascending) of loading the module.
     *
     * @return 0 as default value;
     */
    default int priority() {
        return 0;
    }

    String[] NONE_DEPENDENCIES = new String[]{};

    /**
     * List of modIDs that will be checked for load.
     * If the array is empty, it is ignored.
     *
     * @return {@link Modular#NONE_DEPENDENCIES} as default value.
     */
    default String[] dependencies() {
        return NONE_DEPENDENCIES;
    }

    /**
     * Place for your own conditions for module
     * loading possibility.
     *
     * @return True as default value;
     */
    default boolean canLoad() {
        return true;
    }
}
