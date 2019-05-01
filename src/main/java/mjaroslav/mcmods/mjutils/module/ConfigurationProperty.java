package mjaroslav.mcmods.mjutils.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add this annotation to the field to use it as a
 * configuration option. Your class must be annotated
 * with {@link ConfigurationCategory}. The field type
 * must be an int, boolean, double or String, or an
 * array of one of these types.
 *
 * @see ConfigurationCategory
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigurationProperty {
    String COMMENT_NONE = "Description not provided.";

    /**
     * If not specified, a lowercase field name will be used,
     * the words of which are separated by '{@literal _}'
     *
     * @return Property name or formatter field name.
     */
    String name() default "";

    /**
     * Property description. Should not be null.
     *
     * @return Your value of {@link ConfigurationProperty#COMMENT_NONE}
     * as default value.
     */
    String comment() default COMMENT_NONE;

    /**
     * Default value for int type fields.
     *
     * @return 0 as default value.
     * @see ConfigurationProperty#minInt()
     * @see ConfigurationProperty#maxInt()
     */
    int defaultInt() default 0;

    /**
     * Maximum value for int and int array type fields.
     *
     * @return {@link Integer#MAX_VALUE} as default value.
     */
    int maxInt() default Integer.MAX_VALUE;

    /**
     * Minimum value for int and int array type fields.
     *
     * @return {@link Integer#MIN_VALUE} as default value.
     */
    int minInt() default Integer.MIN_VALUE;

    /**
     * Default value for double type fields.
     *
     * @return 0.0 as default value.
     * @see ConfigurationProperty#minDouble()
     * @see ConfigurationProperty#maxDouble()
     */
    double defaultDouble() default 0.0;

    /**
     * Maximum value for double and double array type fields.
     *
     * @return {@link Double#MAX_VALUE} as default value.
     */
    double maxDouble() default Double.MAX_VALUE;

    /**
     * Minimum value for double and double array type fields.
     *
     * @return {@link Double#MIN_VALUE} as default value.
     */
    double minDouble() default Double.MIN_VALUE;

    /**
     * Default value for boolean type fields.
     *
     * @return False as default value;
     */
    boolean defaultBoolean() default false;

    /**
     * Default value for boolean array type fields.
     *
     * @return Empty array as default value.
     * @see ConfigurationProperty#listLengthFixed()
     * @see ConfigurationProperty#maxListLength()
     */
    boolean[] defaultBooleanArray() default {};

    /**
     * Default value for String type fields.
     *
     * @return Empty string as default value.
     */
    String defaultString() default "";

    /**
     * Default value for int array type fields.
     *
     * @return Empty array as default value.
     * @see ConfigurationProperty#minInt()
     * @see ConfigurationProperty#maxInt()
     * @see ConfigurationProperty#listLengthFixed()
     * @see ConfigurationProperty#maxListLength()
     */
    int[] defaultIntArray() default {};

    /**
     * Default value for double array type fields.
     *
     * @return Empty array as default value.
     * @see ConfigurationProperty#minDouble()
     * @see ConfigurationProperty#maxDouble()
     * @see ConfigurationProperty#listLengthFixed()
     * @see ConfigurationProperty#maxListLength()
     */
    double[] defaultDoubleArray() default {};

    /**
     * Default value for String array type fields.
     *
     * @return Empty array as default value.
     * @see ConfigurationProperty#listLengthFixed()
     * @see ConfigurationProperty#maxListLength()
     */
    String[] defaultStringArray() default {};

    /**
     * Do You need to restart the world, after changing the
     * options within this category?
     *
     * @return False as default value.
     * @see ConfigurationCategory#requiresWorldRestart()
     */
    boolean requiresWorldRestart() default false;

    /**
     * Do You need to restart the game, after changing the
     * options within this category?
     *
     * @return False as default value.
     * @see ConfigurationCategory#requiresMcRestart()
     */
    boolean requiresMcRestart() default false;

    /**
     * Fix the size of the array fields. All default values
     * should be this length. Array should not be dynamic.
     *
     * @return False as default value.
     * @see ConfigurationProperty#maxListLength()
     * @see ConfigurationProperty#defaultDoubleArray()
     * @see ConfigurationProperty#defaultBooleanArray()
     * @see ConfigurationProperty#defaultIntArray()
     * @see ConfigurationProperty#defaultStringArray()
     */
    boolean listLengthFixed() default false;

    /**
     * Maximum length of array fields. If set to -1,
     * the array will be dynamic.
     *
     * @return -1 as default value (array will be dynamic).
     * @see ConfigurationProperty#listLengthFixed()
     * @see ConfigurationProperty#defaultDoubleArray()
     * @see ConfigurationProperty#defaultBooleanArray()
     * @see ConfigurationProperty#defaultIntArray()
     * @see ConfigurationProperty#defaultStringArray()
     */
    int maxListLength() default -1;

    /**
     * The list of valid values is a string; other
     * strings will be ignored. When returning an empty
     * array, any values will be valid.
     *
     * @return Empty array as default value (any values valid).
     */
    String[] validValues() default {};
}