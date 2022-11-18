package io.github.mjaroslav.mjutils.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
}
