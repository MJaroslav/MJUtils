package io.github.mjaroslav.mjutils.util.object;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

/**
 * Special case of {@link Pair} with same typed generics.
 *
 * @param <V> shared type of {@link Pair}.
 * @see Pair
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MonoPair<V> extends Pair<V, V> {
    public MonoPair(@Nullable V x, @Nullable V y) {
        super(x, y);
    }
}
