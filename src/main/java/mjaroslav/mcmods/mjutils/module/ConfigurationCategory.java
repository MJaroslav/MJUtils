package mjaroslav.mcmods.mjutils.module;

import net.minecraftforge.common.config.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add this annotation for use your class as a configuration.
 * You must specify the modID in the upper class. You do not
 * need to do this in the all nested classes-subcategories.
 * For each field that you want to turn into an option, you
 * must add a {@link ConfigurationProperty} annotation. Do not
 * forget about {@link AnnotationBasedConfiguration}, which is
 * responsible for the operation of this feature.
 *
 * @see ConfigurationProperty
 * @see AnnotationBasedConfiguration
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigurationCategory {
    String GENERAL_NAME = Configuration.CATEGORY_GENERAL;
    String COMMENT_NONE = "Description not provided.";
    String GENERAL_COMMENT = "Usually contains client, debug and server configuration categories";

    /**
     * ModID of owner-modification. Used as name of
     * configuration file as well as used in update event.
     * Not used in nested classes.
     *
     * @return ModID.
     */
    String modID() default "";

    /**
     * The name of the configuration category. Must be in lower
     * case. All nested classes themselves supplement their name
     * with the name of the parent category. Should not be null.
     *
     * @return Category name in lower case.
     */
    String name();

    /**
     * Category description. Should not be null.
     *
     * @return Category description or {@link ConfigurationCategory#COMMENT_NONE} as default value.
     */
    String comment() default COMMENT_NONE;

    /**
     * Do You need to restart the world, after changing the
     * options within this category?
     *
     * @return A value of true will also be applied to
     * nested all properties and categories.
     */
    boolean requiresWorldRestart() default false;

    /**
     * Do You need to restart the game, after changing
     * the options within this category?
     *
     * @return A value of true will also be applied to
     * nested all properties and categories.
     */
    boolean requiresMcRestart() default false;
}
