package io.github.mjaroslav.mjutils.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

@AllArgsConstructor
@Getter
public class DelegatingObject<V> {
    protected final @Nullable V value;
    protected final @Nullable BiPredicate<V, Object> equalsDelegate;
    protected final @Nullable ToIntFunction<V> hashCodeDelegate;

    @Contract("_, _ -> new")
    public static <T> @NotNull DelegatingObject<T> of(@Nullable T value, @Nullable ToIntFunction<T> hashCodeHandler) {
        return new DelegatingObject<>(value, null, hashCodeHandler);
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull DelegatingObject<T> of(@Nullable T value, @Nullable BiPredicate<T, Object> equalsHandler) {
        return new DelegatingObject<>(value, equalsHandler, null);
    }

    @Contract("_, _, _ -> new")
    public static <T> @NotNull DelegatingObject<T> of(@Nullable T value, @Nullable BiPredicate<T, Object> equalsHandler,
                                                      @Nullable ToIntFunction<T> hashCodeHandler) {
        return new DelegatingObject<>(value, equalsHandler, hashCodeHandler);
    }

    @Override
    public int hashCode() {
        return value != null && hashCodeDelegate != null
            ? hashCodeDelegate.applyAsInt(value) : Objects.hashCode(value);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return value != null && equalsDelegate != null ? equalsDelegate.test(value,
            obj instanceof DelegatingObject<?> delegate ? delegate.value : obj) : Objects.equals(value,
            obj instanceof DelegatingObject<?> delegate ? delegate.value : obj);
    }
}
