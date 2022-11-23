package io.github.mjaroslav.mjutils.util.object;

import io.github.mjaroslav.mjutils.util.UtilsReflection;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * Special realization of {@link Set} for using {@link DelegatingObject}.
 *
 * @param <T> type that will be used in {@link DelegatingObject}.
 * @see DelegatingObject
 */
@Getter
public class DelegatingSet<T> implements Set<T> {
    /**
     * Equals predicate. Make it null if you want to use original predicate.
     *
     * @see DelegatingObject#equalsDelegate
     */
    protected final @Nullable BiPredicate<T, Object> equalsDelegate;
    /**
     * Hash code function. Make it null if you want to use original function.
     *
     * @see DelegatingObject#hashCodeDelegate
     */
    protected final @Nullable ToIntFunction<T> hashCodeDelegate;
    /**
     * Internal set for backand of this map. Just use {@link HashSet} or
     * {@link DelegatingSet#hashSet(BiPredicate, ToIntFunction)}.
     */
    protected final @NotNull Set<DelegatingObject<T>> impl;
    /**
     * Preserved type generic.
     */
    protected final @NotNull Class<?> genericType;

    public DelegatingSet(@Nullable BiPredicate<T, Object> equalsDelegate, @Nullable ToIntFunction<T> hashCodeDelegate,
                         @NotNull Set<DelegatingObject<T>> impl) {
        this.equalsDelegate = equalsDelegate;
        this.hashCodeDelegate = hashCodeDelegate;
        this.impl = impl;
        genericType = UtilsReflection.getGenericType(getClass(), 0);
    }

    @Override
    public int size() {
        return impl.size();
    }

    @Override
    public boolean isEmpty() {
        return impl.isEmpty();
    }

    @Contract("_ -> new")
    public static @NotNull <T2> DelegatingSet<T2> hashSet(@Nullable ToIntFunction<T2> hashCodeDelegate) {
        return new DelegatingSet<>(null, hashCodeDelegate, new HashSet<>());
    }

    @Contract("_ -> new")
    public static @NotNull <T2> DelegatingSet<T2> hashSet(@Nullable BiPredicate<T2, Object> equalsDelegate) {
        return new DelegatingSet<>(equalsDelegate, null, new HashSet<>());
    }

    @Contract("_, _ -> new")
    public static @NotNull <T2> DelegatingSet<T2> hashSet(@Nullable BiPredicate<T2, Object> equalsDelegate,
                                                          @Nullable ToIntFunction<T2> hashCodeDelegate) {
        return new DelegatingSet<>(equalsDelegate, hashCodeDelegate, new HashSet<>());
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(@Nullable Object o) {
        return o != null && genericType.isAssignableFrom(o.getClass()) &&
            impl.contains(DelegatingObject.of((T) o, equalsDelegate, hashCodeDelegate));
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return impl.stream().map(DelegatingObject::getValue).iterator();
    }

    @Override
    public @Nullable Object @NotNull [] toArray() {
        return impl.stream().map(DelegatingObject::getValue).toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @Override
    public <T1> @Nullable T1 @NotNull [] toArray(@Nullable T1 @NotNull [] a) {
        return impl.stream().map(DelegatingObject::getValue).collect(Collectors.toSet()).toArray(a);
    }

    @Override
    public boolean add(@Nullable T t) {
        return impl.add(DelegatingObject.of(t, equalsDelegate, hashCodeDelegate));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(@Nullable Object o) {
        return o != null && genericType.isAssignableFrom(o.getClass()) && impl.remove(DelegatingObject.
            of((T) o, equalsDelegate, hashCodeDelegate));
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        val size = size();
        c.forEach(this::remove);
        return size() != size;
    }

    @Override
    public void clear() {
        impl.clear();
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return c.stream().map(this::contains).filter(Boolean::valueOf).count() == c.size();
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return impl.addAll(c.stream().map(element -> DelegatingObject.of(element, equalsDelegate, hashCodeDelegate))
            .collect(Collectors.toSet()));
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        var modified = false;
        val it = iterator();
        while (it.hasNext())
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        return modified;
    }

    @Override
    public String toString() {
        return "DelegatingSet@[" + impl.stream().map(DelegatingObject::toString).collect(Collectors.joining()) + "]";
    }
}
