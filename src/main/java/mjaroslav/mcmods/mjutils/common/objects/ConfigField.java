package mjaroslav.mcmods.mjutils.common.objects;

import java.lang.annotation.*;

/**
 * Uses for registration of configuration fields. Field should be public static.
 * 
 * @author MJaroslav
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigField {
    /**
     * Configuration field name. Will use field name if it null or empty.
     * 
     * @return Default "".
     */
    String customName() default "";

    /**
     * Category name (lower case).
     * 
     * @return Default "general".
     */
    String category() default "general";

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
    float defaultFloat() default 0F;

    /**
     * Maximal value of float.
     * 
     * @return Default {@link Float#MAX_VALUE}.
     */
    float maxFloat() default Float.MAX_VALUE;

    /**
     * Minimal value of float.
     * 
     * @return Default {@link Float#MIN_VALUE}.
     */
    float minFloat() default Float.MIN_VALUE;

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
    float[] defaultFloatArray() default {};

    /**
     * Default String array value.
     * 
     * @return Default - empty array.
     */
    String[] defaultStringArray() default {};

    /**
     * It should be true if you use {@link #listLengthFixed()},
     * {@link #maxListLength()} or {@link #validValues()}.
     * 
     * @return Default false.
     */
    boolean advanced() default false;

    /**
     * Fixes the size of the array. The size of the standard value is used.
     * Requires {@link #advanced()}.
     * 
     * @return Default false;
     */
    boolean listLengthFixed() default false;

    /**
     * Maximal array size. Use -1 for disable. Requires {@link #advanced()}.
     * 
     * @return Default -1;
     */
    int maxListLength() default -1;

    /**
     * Valid values for strings. Requires {@link #advanced()}.
     * 
     * @return Default - empty array.
     */
    String[] validValues() default {};
}