package mjaroslav.mcmods.mjutils.common.objects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for modules registration. (Use with {@link IModModule}).
 *
 * @author MJaroslav
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModInitModule {
    /**
     * Modification id.
     *
     * @return Modification id.
     */
    String modid();
}