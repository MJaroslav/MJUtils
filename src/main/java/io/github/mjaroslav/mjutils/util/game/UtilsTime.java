package io.github.mjaroslav.mjutils.util.game;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UtilsTime {
    public final int TICKS_SECOND = 20;
    public final int TICKS_MINUTE = TICKS_SECOND * 60;
    public final int TICKS_HOUR = TICKS_MINUTE * 60;
    public final int TICKS_GAME_DAY = TICKS_MINUTE * 20;
    public final int TICKS_ONE_SMELT = TICKS_SECOND * 10;

    public int getFurnaceSmeltingTicks(float smeltingCount) {
        return (int) (smeltingCount * TICKS_ONE_SMELT);
    }

    public float getFurnaceSmeltingCountFromTicks(int ticks) {
        return (float) (ticks / TICKS_ONE_SMELT);
    }
}
