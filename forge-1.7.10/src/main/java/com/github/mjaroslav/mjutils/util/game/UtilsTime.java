package com.github.mjaroslav.mjutils.util.game;

import com.github.mjaroslav.mjutils.object.game.constant.ConstantsTime;

/**
 * A set of tools to translate time.
 */
public class UtilsTime {
    /**
     * Change seconds to ticks.
     *
     * @param seconds - seconds to change.
     * @return Seconds in ticks.
     */
    public static int getTicksFromSeconds(int seconds) {
        return seconds * ConstantsTime.SECOND;
    }

    /**
     * Change minutes to ticks.
     *
     * @param minutes - minutes to change.
     * @return Minutes in ticks.
     */
    public static int getTicksFromMinutes(int minutes) {
        return minutes * ConstantsTime.MINUTE;
    }

    /**
     * Change seconds and milliseconds to ticks.
     *
     * @param seconds - seconds to change.
     * @param mills   - milliseconds to change.
     * @return Seconds and milliseconds in ticks.
     */
    public static int getTicksFromSeconds(int seconds, int mills) {
        return seconds * ConstantsTime.SECOND + Math.round((mills / 1000) * ConstantsTime.SECOND);
    }

    /**
     * Change minutes and seconds to ticks.
     *
     * @param minutes - minutes to change.
     * @param seconds - seconds to change.
     * @return Seconds to ticks.
     */
    public static int getTicksFromMinutes(int minutes, int seconds) {
        return minutes * ConstantsTime.MINUTE + seconds * ConstantsTime.SECOND;
    }

    /**
     * Change burnt items to ticks.
     *
     * @param count - burnt items.
     * @return Burnt items in ticks.
     */
    public static int getTicksFromSmelting(int count) {
        return count * ConstantsTime.ONE_SMELT;
    }

    /**
     * Change burnt items to ticks.
     *
     * @param count - burnt items.
     * @return Burnt items in ticks.
     */
    public static int getTicksFromSmelting(float count) {
        return (int) (count * ConstantsTime.ONE_SMELT);
    }

    /**
     * Change ticks to burnt items.
     *
     * @param ticks - ticks to change.
     * @return Ticks in burnt items.
     */
    public static float getSmeltingCountFromTicks(int ticks) {
        return (float) ticks / ConstantsTime.ONE_SMELT;
    }
}
