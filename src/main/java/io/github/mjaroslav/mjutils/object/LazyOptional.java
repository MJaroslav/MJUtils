package io.github.mjaroslav.mjutils.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class LazyOptional<T> {
    @Getter
    protected boolean initialized;
    protected @Nullable T value;
    protected final @NotNull Supplier<T> initializer;

    public void initialize() {
        if (!isInitialized()) {
            initialized = true;
            value = initializer.get();
        }
    }

    public @Nullable T get() {
        initialize();
        return value;
    }

    public boolean isPresent() {
        initialize();
        return value != null;
    }

    public boolean isEmpty() {
        initialize();
        return value == null;
    }

    public void ifPresent(@NotNull Consumer<T> action) {
        initialize();
        if (value != null) action.accept(value);
    }

    public void ifPresentOrElse(@NotNull Consumer<T> action, @NotNull Runnable emptyAction) {
        initialize();
        if (value != null) action.accept(value);
        else emptyAction.run();
    }

    @Contract(value = "!null -> !null")
    public @Nullable T orElse(@Nullable T other) {
        initialize();
        return value == null ? other : value;
    }

    public T orElseGet(@NotNull Supplier<T> supplier) {
        initialize();
        return value == null ? supplier.get() : value;
    }

    public @NotNull T orElseThrow() {
        initialize();
        if (value == null) throw new NoSuchElementException("No value present");
        return value;
    }

    public <X extends Throwable> @NotNull T orElseThrow(@NotNull Supplier<? extends X> exceptionSupplier) throws X {
        initialize();
        if (value != null) return value;
        else throw exceptionSupplier.get();
    }

    public @NotNull Stream<T> stream() {
        initialize();
        if (value == null) return Stream.empty();
        else return Stream.of(value);
    }

    @Override
    public int hashCode() {
        initialize();
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        initialize();
        return value != null
            ? String.format("LazyOptional[%s]", value)
            : "LazyOptional.empty";
    }
}
