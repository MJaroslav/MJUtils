package io.github.mjaroslav.mjutils.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pair<X, Y> {
    protected X x;
    protected Y y;

    public void set(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class MonoPair<V> extends Pair<V, V> {
        public MonoPair(V x, V y) {
            super(x, y);
        }
    }
}
