package io.github.mjaroslav.mjutils.config.annotations;

import org.jetbrains.annotations.Range;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ArraySize {
    @Range(from = 0, to = Long.MAX_VALUE) int value();

    boolean fixed() default false;
}
