package com.github.mjaroslav.mjutils.config.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Restart {
    @NotNull Value value() default Value.GAME;

    enum Value {
        WORLD, GAME, BOTH
    }
}
