package io.github.mjaroslav.mjutils.modular;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SubscribeSidedOnlyModule {
    int priority() default Integer.MAX_VALUE;
}
