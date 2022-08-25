package com.github.mjaroslav.mjutils.object;

import com.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class DelegatingSet<T> implements Set<T> {
    public final @Nullable BiPredicate<T, Object> equalsHandler;
    public final @Nullable ToIntFunction<T> hashCodeHandler;
    public final @NotNull Set<Wrapper<T>> impl;
    public final @NotNull Class<?> cachedKeyClass;

    public DelegatingSet(@Nullable BiPredicate<T, Object> equalsHandler, @Nullable ToIntFunction<T> hashCodeHandler,
                         @NotNull Set<Wrapper<T>> impl) {
        this.equalsHandler = equalsHandler;
        this.hashCodeHandler = hashCodeHandler;
        this.impl = impl;
        cachedKeyClass = UtilsReflection.getParameterizedClass(getClass(), 0);
    }

    @Override
    public int size() {
        return impl.size();
    }

    @Override
    public boolean isEmpty() {
        return impl.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(@Nullable Object o) {
        if (o != null && cachedKeyClass.isAssignableFrom(o.getClass()))
            return impl.contains(new Wrapper<>((T) o, equalsHandler, hashCodeHandler));
        return false;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return impl.stream().map(wrapper -> wrapper.value).iterator();
    }

    @Override
    public @Nullable Object @NotNull [] toArray() {
        return impl.stream().map(wrapper -> wrapper.value).toArray();
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    @Override
    public <T1> @Nullable T1 @NotNull [] toArray(@Nullable T1 @NotNull [] a) {
        return impl.stream().map(wrapper -> wrapper.value).collect(Collectors.toSet()).toArray(a);
    }

    @Override
    public boolean add(@Nullable T t) {
        return impl.add(new Wrapper<>(t, equalsHandler, hashCodeHandler));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(@Nullable Object o) {
        if (o != null && cachedKeyClass.isAssignableFrom(o.getClass()))
            return impl.remove(new Wrapper<>((T) o, equalsHandler, hashCodeHandler));
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return c.stream().map(this::contains).filter(element -> element).count() == c.size();
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return impl.addAll(c.stream().map(element -> new Wrapper<>(element, equalsHandler, hashCodeHandler))
                .collect(Collectors.toSet()));
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        boolean modified = false;
        val it = iterator();
        while (it.hasNext())
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        return modified;
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

    @AllArgsConstructor
    public static class Wrapper<T> {
        public final @Nullable T value;
        public final @Nullable BiPredicate<T, Object> equalsHandler;
        public final @Nullable ToIntFunction<T> hashCodeHandler;

        @Override
        public int hashCode() {
            return value != null && hashCodeHandler != null
                    ? hashCodeHandler.applyAsInt(value) : Objects.hashCode(value);
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(@Nullable Object obj) {
            return value != null && equalsHandler != null
                    ? equalsHandler.test(value, obj) : Objects.equals(value, obj);
        }
    }

    public static @NotNull <T2> DelegatingSet<T2> byHashSet(@Nullable BiPredicate<T2, Object> equalsHandler,
                                                            @Nullable ToIntFunction<T2> hashCodeHandler) {
        return new DelegatingSet<>(equalsHandler, hashCodeHandler, new HashSet<>());
    }
}
