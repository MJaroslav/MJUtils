package io.github.mjaroslav.mjutils.util.net;

import io.github.mjaroslav.mjutils.mod.lib.ModInfo;
import io.github.mjaroslav.mjutils.util.object.Lazy;
import io.github.mjaroslav.mjutils.util.object.Pair;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.net.URI;

@UtilityClass
public class UtilsDesktop {
    private final Lazy<Pair<Method, Object>> DESKTOP = new Lazy<>(() -> {
        try {
            val clazz = Class.forName("java.awt.Desktop");
            val method = clazz.getMethod("browse", URI.class);
            val object = clazz.getMethod("getDesktop").invoke(null);
            return new Pair<>(method, object);
        } catch (Throwable e) {
            ModInfo.loggerLibrary.error("Desktop not available, HOW?", e);
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
                ModInfo.loggerLibrary.error("Couldn't open link", e);
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
