package io.github.mjaroslav.mjutils.util.object;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

/**
 * Special case of {@link Trio} with same typed generics.
 *
 * @param <V> shared type of {@link Trio}.
 * @see Trio
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MonoTrio<V> extends Trio<V, V, V> {
    public MonoTrio(@Nullable V x, @Nullable V y, @Nullable V z) {
        super(x, y, z);
    }
}
