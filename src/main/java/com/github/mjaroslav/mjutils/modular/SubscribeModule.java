package com.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.LoaderState.ModState;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SubscribeModule {
    @Nonnull String value() default "";

    @Nonnull String[] modDependencies() default {};

    int priority() default 0;

    @Nonnull ModState loadOn() default ModState.CONSTRUCTED;
}
