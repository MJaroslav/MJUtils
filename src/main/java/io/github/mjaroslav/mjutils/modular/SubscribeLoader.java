package io.github.mjaroslav.mjutils.modular;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ModuleLoader marker. You should use this on public and
 * static field with type {@link ModuleLoader} in your main mod class.
 * <br>
 * See also {@link ModuleLoader}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SubscribeLoader {
    /**
     * Custom root package for module finder.
     * As default value used package of main mod class
     * (class where this annotation used).
     * <br>
     * Note that if {@link SubscribeModule#value()} present in module,
     * loader find it from any package.
     *
     * @return Package in java format or empty string for default value.
     */
    @NotNull String value() default "";
}
