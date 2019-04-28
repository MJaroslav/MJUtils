package mjaroslav.mcmods.mjutils.lib.module;

import java.lang.annotation.*;

/**
 * Used for modules registration. (Use with {@link Modular}).
 *
 * @author MJaroslav
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Module {
    /**
     * Modification id.
     *
     * @return Modification id.
     */
    String modid();
}