package io.github.mjaroslav.mjutils.util;

import io.github.mjaroslav.mjutils.lib.MJUtilsInfo;
import io.github.mjaroslav.sharedjava.function.LazySupplier;
import io.github.mjaroslav.sharedjava.tuple.Pair;
import io.github.mjaroslav.sharedjava.tuple.pair.SimplePair;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.net.URI;

/**
 * Utilities for manipulating with PC and game.
 */
@UtilityClass
public class UtilsDesktop {
    private final LazySupplier<Pair<Method, Object>> DESKTOP = new LazySupplier<>(() -> {
        try {
            val clazz = Class.forName("java.awt.Desktop");
            val method = clazz.getMethod("browse", URI.class);
            val object = clazz.getMethod("getDesktop").invoke(null);
            return new SimplePair<>(method, object);
        } catch (Throwable e) {
            MJUtilsInfo.LOG_LIB.error("Desktop not available, HOW?", e);
            return null;
        }
    });

    /**
     * Open URI by {@link java.awt.Desktop Desktop} if it presented.
     *
     * @param uri URI for open
     */
    public void openURL(@NotNull URI uri) {
        DESKTOP.ifPresent(pair -> {
            try {
                pair.getX().invoke(pair.getY(), uri);
            } catch (Throwable e) {
                MJUtilsInfo.LOG_LIB.error("Couldn't open link", e);
            }
        });
    }

    /**
     * Crash game by {@link ReportedException}.
     *
     * @param cause       game crash cause, some exceptions may be wrapped by {@link CrashReport}.
     * @param description game crash cause description.
     */
    public void crashGame(@Nullable Throwable cause, @NotNull String description) {
        throw new ReportedException(CrashReport.makeCrashReport(cause, description));
    }
}
