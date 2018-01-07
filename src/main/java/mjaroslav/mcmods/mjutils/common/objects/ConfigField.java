package mjaroslav.mcmods.mjutils.common.objects;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigField {
    String customName() default "";

    String category() default "general";

    String comment();

    String langKey() default "";

    int defaultInt() default 0;

    int maxInt() default Integer.MAX_VALUE;

    int minInt() default Integer.MIN_VALUE;

    float defaultFloat() default 0F;

    float maxFloat() default Float.MAX_VALUE;

    float minFloat() default Float.MIN_VALUE;

    String defaultString() default "";

    int[] defaultIntArray() default {};

    float[] defaultFloatArray() default {};

    String[] defaultStringArray() default {};
}