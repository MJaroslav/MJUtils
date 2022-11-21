package io.github.mjaroslav.mjutils.util.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * Holder for two objects. Why it is not added to Java yet?
 * <p>
 * For pairs with same types generics you can use {@link MonoPair}.
 *
 * @param <X> first element, "x" name for ease of use as a pair of coordinates and more.
 * @param <Y> second element, "y" name for ease of use as a pair of coordinates and more.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pair<X, Y> {
    /**
     * First element of pair.
     */
    protected X x;
    /**
     * Second element of pair.
     */
    protected Y y;

    /**
     * Set new values.
     *
     * @param x first element.
     * @param y second element.
     */
    public void set(@Nullable X x, @Nullable Y y) {
        setX(x);
        setY(y);
    }
}
