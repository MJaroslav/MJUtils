package io.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.event.FMLEvent;
import io.github.mjaroslav.mjutils.asm.mixin.accessors.AccessorFMLModContainer;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import io.github.mjaroslav.sharedjava.function.LazySupplier;
import io.github.mjaroslav.sharedjava.reflect.ReflectionHelper;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Objects;

import static io.github.mjaroslav.mjutils.lib.MJUtilsInfo.*;

@Getter
public final class ModuleInfo implements Comparable<ModuleInfo> {
    private final @NotNull String @NotNull [] modDependencies;
    private final int priority;
    private final @NotNull ModState loadOn;
    private final @NotNull String moduleClassName;
    private final @NotNull LazySupplier<Object> lazyModule;
    private final @NotNull LazySupplier<Boolean> lazyIsAllRequiredModsLoaded;

    ModuleInfo(@NotNull Object proxy, int priority) {
        modDependencies = new String[0];
        this.priority = priority;
        loadOn = ModState.CONSTRUCTED;
        moduleClassName = proxy.getClass().getName();
        lazyModule = new LazySupplier<>(() -> proxy);
        lazyIsAllRequiredModsLoaded = new LazySupplier<>(() -> true);
    }

    ModuleInfo(@NotNull String @NotNull [] modDependencies, int priority, @NotNull LoaderState.ModState loadOn,
               @NotNull String moduleClassName) {
        this.modDependencies = modDependencies;
        this.priority = priority;
        this.loadOn = loadOn;
        this.moduleClassName = moduleClassName;
        lazyModule = new LazySupplier<>(() -> {
            try {
                LOG_MODULES.debug(String.format("Creating instance of %s module", moduleClassName));
                return Class.forName(moduleClassName).getConstructor().newInstance();
            } catch (Exception e) {
                LOG_MODULES.error(String.format("Can't load %s module", moduleClassName), e);
                return null;
            }
        });
        lazyIsAllRequiredModsLoaded = new LazySupplier<>(() -> UtilsMods.isModsLoaded(modDependencies));
    }


    public boolean isAllRequiredModsLoaded() {
        return lazyIsAllRequiredModsLoaded.orElse(false);
    }

    public void tryListen(@NotNull FMLModContainer container, @NotNull FMLEvent event) {
        lazyModule.ifPresent(module -> {
            try {
                module.getClass().getMethod("listen", event.getClass()).invoke(module, event);
            } catch (NoSuchMethodException ignored) {
                LOG_MODULES.trace(String.format("Can't find listener for %s event in %s module. " +
                        "It's not an error message if you not write listener for this", event.getEventType(),
                    moduleClassName));
            } catch (InvocationTargetException e) {
                // Rethrow exception
                ((AccessorFMLModContainer) container).getController().errorOccurred(container, e.getCause());
            } catch (IllegalAccessException e) {
                LOG_MODULES.warn(String.format("Listener for %s event in %s module will ignored by bad " +
                    "syntax, them must be non-static and public", event.getEventType(), moduleClassName));
            }
        });
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModuleClassName());
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ModuleInfo that)) return false;
        return Objects.equals(getModuleClassName(), that.getModuleClassName());
    }

    @Override
    public @NotNull String toString() {
        return ReflectionHelper.getSimpleClassName(getModuleClassName());
    }

    @Override
    public int compareTo(@NotNull ModuleInfo o) {
        val priorityCompare = Comparator.comparingInt(ModuleInfo::getPriority).compare(this, o);
        return priorityCompare != 0 ? priorityCompare : Comparator.comparing(ModuleInfo::getModuleClassName).compare(this, o);
    }
}
