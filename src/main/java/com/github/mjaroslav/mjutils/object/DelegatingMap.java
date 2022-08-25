package com.github.mjaroslav.mjutils.object;

import com.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@Getter
public class DelegatingMap<K, V> implements Map<K, V> {
    public final @Nullable BiPredicate<K, Object> equalsHandler;
    public final @Nullable ToIntFunction<K> hashCodeHandler;
    public final @NotNull Map<Wrapper<K>, V> impl;
    public final @NotNull Class<?> cachedKeyClass;

    public DelegatingMap(@Nullable BiPredicate<K, Object> equalsHandler, @Nullable ToIntFunction<K> hashCodeHandler,
                         @NotNull Map<Wrapper<K>, V> impl) {
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

    @Override
    public boolean containsKey(@Nullable Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        return impl.containsValue(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable V get(@Nullable Object key) {
        if (key != null && cachedKeyClass.isAssignableFrom(key.getClass()))
            return impl.get(new Wrapper<>((K) key, equalsHandler, hashCodeHandler));
        else return null;
    }

    @Override
    public @Nullable V put(@Nullable K key, @Nullable V value) {
        return impl.put(new Wrapper<>(key, equalsHandler, hashCodeHandler), value);
    }


    @Override
    public @Nullable V remove(Object key) {
        val value = get(key);
        return value == null ? null : impl.remove(value);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        m.forEach((key, value) -> impl.put(new Wrapper<>(key, equalsHandler, hashCodeHandler), value));
    }

    @Override
    public void clear() {
        impl.clear();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return impl.keySet().stream().map(wrapper -> wrapper.key).collect(Collectors.toSet());
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return impl.values();
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return impl.entrySet().stream().map(entry ->
                        new AbstractMap.SimpleEntry<>(entry.getKey().key, entry.getValue()))
                .collect(Collectors.toSet());
    }

    @AllArgsConstructor
    public static class Wrapper<K> {
        public final @Nullable K key;
        public final @Nullable BiPredicate<K, Object> equalsHandler;
        public final @Nullable ToIntFunction<K> hashCodeHandler;

        @Override
        public int hashCode() {
            return key != null && hashCodeHandler != null
                    ? hashCodeHandler.applyAsInt(key) : Objects.hashCode(key);
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(@Nullable Object obj) {
            return key != null && equalsHandler != null
                    ? equalsHandler.test(key, obj) : Objects.equals(key, obj);
        }
    }

    public static @NotNull <K2, V2> DelegatingMap<K2, V2> byHashMap(@Nullable BiPredicate<K2, Object> equalsHandler,
                                                                    @Nullable ToIntFunction<K2> hashCodeHandler) {
        return new DelegatingMap<>(equalsHandler, hashCodeHandler, new HashMap<>());
    }
}
