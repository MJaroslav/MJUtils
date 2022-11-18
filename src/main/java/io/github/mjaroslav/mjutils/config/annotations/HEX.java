package io.github.mjaroslav.mjutils.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see io.github.mjaroslav.mjutils.config.ForgeAnnotationConfig ForgeAnnotationConfig
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HEX {
    // Создано для маркирования Неведомой Ёбаной Хуйни :)
    boolean alpha() default false;
}
