package io.github.mjaroslav.mjutils.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class Lazy<T> {
    protected final @NotNull Supplier<T> initializer;
    protected T value;
    protected @Getter boolean initialized;

    public @NotNull T get() {
        if (!initialized) {
            value = Objects.requireNonNull(initializer.get());
            initialized = true;
        }
        return value;
    }
}
