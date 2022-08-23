package com.github.mjaroslav.mjutils.configurator.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Default {
    int i() default 0;

    double d() default 0;

    boolean b() default false;

    @NotNull String s() default "";

    int @NotNull [] I() default {};

    double @NotNull [] D() default {};

    boolean @NotNull [] B() default {};

    @NotNull String @NotNull [] S() default {};
}
