package io.github.mjaroslav.mjutils.object;

import io.github.mjaroslav.mjutils.util.lang.reflect.UtilsReflection;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

@Getter
public class DelegatingMap<K, V> implements Map<K, V> {
    protected final @Nullable BiPredicate<K, Object> equalsDelegate;
    protected final @Nullable ToIntFunction<K> hashCodeDelegate;
    protected final @NotNull Map<DelegatingObject<K>, V> impl;
    protected final @NotNull Class<?> genericType;

    public DelegatingMap(@Nullable BiPredicate<K, Object> equalsDelegate, @Nullable ToIntFunction<K> hashCodeDelegate,
                         @NotNull Map<DelegatingObject<K>, V> impl) {
        this.equalsDelegate = equalsDelegate;
        this.hashCodeDelegate = hashCodeDelegate;
        this.impl = impl;
        genericType = UtilsReflection.getParameterizedClass(getClass(), 0);
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

    @Contract("_ -> new")
    public static @NotNull <K2, V2> DelegatingMap<K2, V2> hashMap(@Nullable BiPredicate<K2, Object> equalsDelegate) {
        return new DelegatingMap<>(equalsDelegate, null, new HashMap<>());
    }

    @Contract("_ -> new")
    public static @NotNull <K2, V2> DelegatingMap<K2, V2> hashMap(@Nullable ToIntFunction<K2> hashCodeDelegate) {
        return new DelegatingMap<>(null, hashCodeDelegate, new HashMap<>());
    }

    @Override
    public @Nullable V remove(Object key) {
        val value = get(key);
        return value == null ? null : impl.remove(value);
    }

    @Contract("_, _ -> new")
    public static @NotNull <K2, V2> DelegatingMap<K2, V2> hashMap(@Nullable BiPredicate<K2, Object> equalsDelegate,
                                                                  @Nullable ToIntFunction<K2> hashCodeDelegate) {
        return new DelegatingMap<>(equalsDelegate, hashCodeDelegate, new HashMap<>());
    }

    @Override
    public void clear() {
        impl.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable V get(@Nullable Object key) {
        return key != null && genericType.isAssignableFrom(key.getClass()) ?
            impl.get(DelegatingObject.of((K) key, equalsDelegate, hashCodeDelegate)) : null;
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return impl.values();
    }

    @Override
    public @Nullable V put(@Nullable K key, @Nullable V value) {
        return impl.put(DelegatingObject.of(key, equalsDelegate, hashCodeDelegate), value);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        m.forEach((key, value) -> impl.put(DelegatingObject.of(key, equalsDelegate, hashCodeDelegate), value));
    }

    @NotNull
    @Override
    public DelegatingSet<K> keySet() {
        val result = DelegatingSet.hashSet(equalsDelegate, hashCodeDelegate);
        impl.keySet().stream().map(DelegatingObject::getValue).forEach(result::add);
        return result;
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return impl.entrySet().stream().map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey().getValue(),
            entry.getValue())).collect(Collectors.toSet());
    }
}
