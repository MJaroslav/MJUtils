package mjaroslav.mcmods.mjutils.module;

import net.minecraftforge.common.config.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Uses for registration of configuration category. Fields should be with
 * {@link ConfigurationProperty} annotation.
 *
 * @author MJaroslav
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigurationCategory {
    String DEFAULT_NAME = Configuration.CATEGORY_GENERAL;
    String DEFAULT_COMMENT = "Description not provided.";
    boolean DEFAULT_REQUIRES_WORLD_RESTART = false;
    boolean DEFAULT_REQUIRED_MC_RESTART = false;

    String modID();

    String name() default DEFAULT_NAME;

    String comment() default DEFAULT_COMMENT;

    boolean requiresWorldRestart() default DEFAULT_REQUIRES_WORLD_RESTART;

    boolean requiresMcRestart() default DEFAULT_REQUIRED_MC_RESTART;
}
