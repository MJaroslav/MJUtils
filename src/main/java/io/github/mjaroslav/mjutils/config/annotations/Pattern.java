package io.github.mjaroslav.mjutils.config.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see io.github.mjaroslav.mjutils.config.ForgeAnnotationConfig ForgeAnnotationConfig
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Pattern {
    @NotNull String value();
}
