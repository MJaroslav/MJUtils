package mjaroslav.mcmods.mjutils.lib.module;

import java.lang.annotation.*;

/**
 * Used for modules registration. (Use with {@link IModule}).
 *
 * @author MJaroslav
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModModule {
    /**
     * Modification id.
     *
     * @return Modification id.
     */
    String modid();
}