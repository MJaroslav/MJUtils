package io.github.mjaroslav.mjutils.util.object;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Special case of {@link MonoTrio} for numbers. This realization can't take null values.
 *
 * @param <T> type of {@link Number}.
 * @see MonoTrio
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NumberTrio<T extends Number> extends MonoTrio<T> {
    public NumberTrio(@NotNull T x, @NotNull T y, @NotNull T z) {
        super(x, y, z);
    }

    @Override
    public void setX(@NotNull T x) {
        super.setX(Objects.requireNonNull(x));
    }

    @Override
    public void setY(@NotNull T y) {
        super.setY(Objects.requireNonNull(y));
    }

    @Override
    public void setZ(@NotNull T z) {
        super.setZ(Objects.requireNonNull(z));
    }

    /**
     * Return trio with values in int representation.
     *
     * @return new trio with cast values to int by {@link Number#intValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberTrio<Integer> intValue() {
        return new NumberTrio<>(getX().intValue(), getY().intValue(), getZ().intValue());
    }

    /**
     * Return trio with values in long representation.
     *
     * @return new trio with cast values to long by {@link Number#longValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberTrio<Long> longValue() {
        return new NumberTrio<>(getX().longValue(), getY().longValue(), getZ().longValue());
    }

    /**
     * Return trio with values in short representation.
     *
     * @return new trio with cast values to short by {@link Number#shortValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberTrio<Short> shortValue() {
        return new NumberTrio<>(getX().shortValue(), getY().shortValue(), getZ().shortValue());
    }

    /**
     * Return trio with values in double representation.
     *
     * @return new trio with cast values to double by {@link Number#doubleValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberTrio<Double> doubleValue() {
        return new NumberTrio<>(getX().doubleValue(), getY().doubleValue(), getZ().doubleValue());
    }

    /**
     * Return trio with values in float representation.
     *
     * @return new trio with cast values to float by {@link Number#floatValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberTrio<Float> floatValue() {
        return new NumberTrio<>(getX().floatValue(), getY().floatValue(), getZ().floatValue());
    }

    /**
     * Return trio with values in byte representation.
     *
     * @return new trio with cast values to byte by {@link Number#byteValue()}.
     */
    @Contract(" -> new")
    public @NotNull NumberTrio<Byte> byteValue() {
        return new NumberTrio<>(getX().byteValue(), getY().byteValue(), getZ().byteValue());
    }
}
