package io.github.mjaroslav.mjutils.util.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Wrapper of object for replacing {@link Object#toString()} and {@link Object#hashCode()} methods.
 *
 * @param <V> type of original object.
 * @see DelegatingMap
 * @see DelegatingSet
 */
@AllArgsConstructor
@Getter
public class DelegatingObject<V> {
    /**
     * Original value. Can be null, but make sure that it allowed in your delegating collection.
     */
    protected final @Nullable V value;
    /**
     * Equals predicate. Make it null if you want to use original predicate.
     */
    protected final @Nullable BiPredicate<V, Object> equalsDelegate;
    /**
     * Hash code function. Make it null if you want to use original function.
     */
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
