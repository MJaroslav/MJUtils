package mjaroslav.mcmods.mjutils.module;

import java.lang.annotation.*;

import net.minecraftforge.common.config.Configuration;

/**
 * Uses for registration of configuration category. Fields should be with
 * {@link ConfigField} annotation.
 * 
 * @author MJaroslav
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigCategory {
    String modid();

    String name() default Configuration.CATEGORY_GENERAL;

    String comment() default "No description.";

    boolean requiresWorldRestart() default false;

    boolean requiresMcRestart() default false;
}
