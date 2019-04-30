package mjaroslav.mcmods.mjutils.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Uses for registration of configuration fields. Field should be public static.
 *
 * @author MJaroslav
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigurationProperty {
    /**
     * Configuration field name. Will use field name if it null or empty.
     *
     * @return Default "".
     */
    String name() default "";

    /**
     * Comment on the configuration parameter.
     *
     * @return Default "No description".
     */
    String comment() default "No description.";

    /**
     * Default value for int type.
     *
     * @return Default 0.
     */
    int defaultInt() default 0;

    /**
     * Maximal value of int.
     *
     * @return Default {@link Integer#MAX_VALUE}.
     */
    int maxInt() default Integer.MAX_VALUE;

    /**
     * Minimal value of int.
     *
     * @return Default {@link Integer#MIN_VALUE}.
     */
    int minInt() default Integer.MIN_VALUE;

    /**
     * Default float value.
     *
     * @return Default 0F.
     */
    double defaultDouble() default 0F;

    /**
     * Maximal value of float.
     *
     * @return Default {@link Double#MAX_VALUE}.
     */
    double maxDouble() default Double.MAX_VALUE;

    /**
     * Minimal value of float.
     *
     * @return Default {@link Double#MIN_VALUE}.
     */
    double minDouble() default Double.MIN_VALUE;

    /**
     * Default boolean value.
     *
     * @return Default false.
     */
    boolean defaultBoolean() default false;

    /**
     * Default boolean array value.
     *
     * @return Default - empty array.
     */
    boolean[] defaultBooleanArray() default {};

    /**
     * Default String value.
     *
     * @return Default "".
     */
    String defaultString() default "";

    /**
     * Default int array value.
     *
     * @return Default - empty array;
     */
    int[] defaultIntArray() default {};

    /**
     * Default float array value.
     *
     * @return Default - empty array.
     */
    double[] defaultDoubleArray() default {};

    /**
     * Default String array value.
     *
     * @return Default - empty array.
     */
    String[] defaultStringArray() default {};

    boolean requiresWorldRestart() default false;

    boolean requiresMcRestart() default false;

    /**
     * Fixes the size of the array. The size of the standard value is used.
     *
     * @return Default false;
     */
    boolean listLengthFixed() default false;

    /**
     * Maximal array size. Use -1 for disable.
     *
     * @return Default -1;
     */
    int maxListLength() default -1;

    /**
     * Valid values for strings.
     *
     * @return Default - empty array.
     */
    String[] validValues() default {};

    boolean useFloat() default false;
}