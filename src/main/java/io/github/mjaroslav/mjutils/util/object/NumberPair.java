package io.github.mjaroslav.mjutils.util.object;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Special case of {@link MonoPair} for numbers. This realization can't take null values.
 *
 * @param <T> type of {@link Number}.
 * @see MonoPair
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NumberPair<T extends Number> extends MonoPair<T> {
    public NumberPair(@NotNull T x, @NotNull T y) {
        super(x, y);
    }

    @Override
    public void setX(@NotNull T x) {
        super.setX(Objects.requireNonNull(x));
    }

    @Override
    public void setY(@NotNull T y) {
        super.setY(Objects.requireNonNull(y));
    }

    /**
     * Return pair with values in int representation.
     *
     * @return new pair with cast values to int by {@link Number#intValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberPair<Integer> intValue() {
        return new NumberPair<>(getX().intValue(), getY().intValue());
    }

    /**
     * Return pair with values in long representation.
     *
     * @return new pair with cast values to long by {@link Number#longValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberPair<Long> longValue() {
        return new NumberPair<>(getX().longValue(), getY().longValue());
    }

    /**
     * Return pair with values in short representation.
     *
     * @return new pair with cast values to short by {@link Number#shortValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberPair<Short> shortValue() {
        return new NumberPair<>(getX().shortValue(), getY().shortValue());
    }

    /**
     * Return pair with values in double representation.
     *
     * @return new pair with cast values to double by {@link Number#doubleValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberPair<Double> doubleValue() {
        return new NumberPair<>(getX().doubleValue(), getY().doubleValue());
    }

    /**
     * Return pair with values in float representation.
     *
     * @return new pair with cast values to float by {@link Number#floatValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberPair<Float> floatValue() {
        return new NumberPair<>(getX().floatValue(), getY().floatValue());
    }

    /**
     * Return pair with values in byte representation.
     *
     * @return new pair with cast values to byte by {@link Number#byteValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberPair<Byte> byteValue() {
        return new NumberPair<>(getX().byteValue(), getY().byteValue());
    }
}
