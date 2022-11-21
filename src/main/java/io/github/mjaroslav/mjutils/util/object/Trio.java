package io.github.mjaroslav.mjutils.util.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * Holder for three objects, it's like a {@link Pair} but for three objects.
 * <p>
 * For pairs with same types generics you can use {@link MonoTrio}.
 *
 * @param <X> first element, "x" name for ease of use as a pair of coordinates and more.
 * @param <Y> second element, "y" name for ease of use as a pair of coordinates and more.
 * @param <Z> third element, "z" name for ease of use as a pair of coordinates and more.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trio<X, Y, Z> {
    /**
     * First element of trio.
     */
    protected X x;
    /**
     * Second element of trio.
     */
    protected Y y;
    /**
     * Third element of trio.
     */
    protected Z z;

    /**
     * Set new values.
     *
     * @param x first element.
     * @param y second element.
     * @param z third element.
     */
    public void set(@Nullable X x, @Nullable Y y, @Nullable Z z) {
        setX(x);
        setY(y);
        setZ(z);
    }
}
