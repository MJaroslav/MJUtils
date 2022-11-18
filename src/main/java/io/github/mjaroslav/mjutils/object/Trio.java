package io.github.mjaroslav.mjutils.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trio<X, Y, Z> {
    protected X x;
    protected Y y;
    protected Z z;

    public void set(X x, Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class MonoTrio<V> extends Trio<V, V, V> {
        public MonoTrio(V x, V y, V z) {
            super(x, y, z);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class NumberTrio<T extends Number> extends MonoTrio<T> {
        public NumberTrio(@NotNull T x, @NotNull T y, @NotNull T z) {
            super(x, y, z);
        }

        @Override
        public void set(@NotNull T t, @NotNull T t2, @NotNull T t3) {
            super.set(t, t2, t3);
        }
    }
}
