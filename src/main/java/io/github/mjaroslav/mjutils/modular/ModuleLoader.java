package io.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLEvent;
import io.github.mjaroslav.mjutils.asm.mixin.accessors.AccessorEnumHolder;
import io.github.mjaroslav.mjutils.util.UtilsDesktop;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import io.github.mjaroslav.sharedjava.reflect.ReflectionHelper;
import io.github.mjaroslav.sharedjava.util.DelegatingMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static io.github.mjaroslav.mjutils.lib.ModInfo.*;
import static io.github.mjaroslav.sharedjava.reflect.ReflectionHelper.*;

@Getter
@RequiredArgsConstructor
public final class ModuleLoader {
    // equals and hashCode by modId
    private static final Map<ModContainer, ModuleLoader> LOADERS_CACHE = new DelegatingMap<>((unit, objUnit) ->
        objUnit != null && objUnit.getX() instanceof ModContainer objMod &&
            Objects.equals(unit.getX().getModId(), objMod.getModId()),
        unit -> Objects.hash(unit.getX().getModId()), null);

    private final @NotNull String modId;
    private final @UnknownNullability String modName;
    private final @NotNull Object modInstance;
    private final @NotNull String rootPackage;
    private final List<ModuleInfo> modules = new ArrayList<>();
    private int foundModulesCount;
    private int activatedModulesCount;

    public static @Nullable ModuleLoader getActiveLoader() {
        return LOADERS_CACHE.get(Loader.instance().activeModContainer());
    }

    public static @Nullable ModuleLoader getOrCreate(@NotNull ModContainer container) {
        val modName = container.getName();
        val modId = container.getModId();
        if (LOADERS_CACHE.containsKey(container)) {
            LOG_MODULES.debug(String.format("Calling of already created ModuleLoader for %s (%s) mod",
                modName, modId));
            return LOADERS_CACHE.get(container);
        } else {
            LOG_MODULES.trace(String.format("Requested creating of ModuleLoader for %s (%s) mod", modName, modId));
            val modInstance = container.getMod();
            val modInstanceClass = modInstance.getClass();
            for (val field : modInstanceClass.getFields())
                if (field.isAnnotationPresent(SubscribeLoader.class)) {
                    val name = field.getName();
                    LOG_MODULES.debug(String.format("Found annotated with @SubscribeLoader %s field in %s (%s) mod",
                        name, modName, modId));
                    var packageName = field.getAnnotation(SubscribeLoader.class).value();
                    if (StringUtils.isEmpty(packageName)) {
                        LOG_MODULES.trace(String.format("Annotated with @SubscribeLoader %s field use empty value " +
                            "for root package name or not specify it. It's not an error message if you want use " +
                            "package from mod object for it", name));
                        // ReflectionHelper from Forge and Shared-Java with same name and in one place, I'm megamind
                        packageName = getPackage(modInstance);
                    }
                    if (Modifier.isStatic(field.getModifiers()) && ModuleLoader.class.isAssignableFrom(field.getType())) {
                        val loader = new ModuleLoader(modId, modName, modInstance, packageName);
                        try {
                            field.set(null, loader);
                        } catch (Exception e) {
                            UtilsDesktop.crashGame(e, String.format("Can't place ModuleLoader to %s field in %s " +
                                "class of %s (%s) mod", field.getName(), modInstanceClass.getName(), modName, modId));
                            return null;
                        }
                        LOG_MODULES.info(String.format("Found and registered ModuleLoader for %s (%s) mod", modName, modId));
                        LOADERS_CACHE.put(container, loader);
                        return loader;
                    } else {
                        UtilsDesktop.crashGame(new IllegalAccessException(), String.format("Can't place ModuleLoader " +
                            "to %s field in %s class of %s (%s) mod. Field must be ModuleLoader typed and be " +
                            "public and static", field.getName(), modInstanceClass.getName(), modName, modId));
                        return null;
                    }
                }
            LOG_MODULES.trace(String.format("No ModuleLoader for %s (%s) mod exists", modName, modId));
            LOADERS_CACHE.put(container, null);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void parseModule(@NotNull ASMData rawModule) {
        val className = rawModule.getClassName();
        val simpleName = ReflectionHelper.getSimpleClassName(className);
        val annotationInfo = rawModule.getAnnotationInfo();
        var modDependencies = new String[0];
        if (annotationInfo.containsKey("modDependencies"))
            modDependencies = ((List<String>) annotationInfo.get("modDependencies")).toArray(modDependencies);
        val priority = (int) annotationInfo.getOrDefault("priority", 0);
        var loadOn = ModState.CONSTRUCTED;
        if (annotationInfo.containsKey("loadOn"))
            loadOn = ModState.valueOf(((AccessorEnumHolder) annotationInfo.get("loadOn")).getValue());
        val info = new ModuleInfo(modDependencies, priority, loadOn, rawModule.getClassName());
        LOG_MODULES.debug(String.format("Found %s module for %s (%s) mod with %s priority that will load on %s state " +
                "and only if next mods are loaded: [%s]", simpleName, modName, modId, priority, loadOn,
            String.join(", ", modDependencies)));
        if (info.isAllRequiredModsLoaded()) modules.add(info);
        else
            LOG_MODULES.info(String.format("Module %s from %s (%s) mod not activated because one or more of next " +
                "dependencies not loaded: [%s]", simpleName, modName, modId, String.join(", ", modDependencies)));
        foundModulesCount++;
    }

    public void tryFindAndAddProxy() {
        val proxy = UtilsMods.getProxyObjectFromMod(modInstance);
        if (proxy != null) {
            modules.add(new ModuleInfo(proxy));
            foundModulesCount++;
            LOG_MODULES.debug(String.format("Found proxy for %s (%s) mod", modName, modId));
        }
    }

    public void completeConstruction() {
        modules.sort(Comparator.comparingInt(ModuleInfo::getPriority));
        activatedModulesCount = modules.size();
        LOG_MODULES.info(String.format("Found %s and activated %s modules for %s (%s) mod: [%s]",
            foundModulesCount, activatedModulesCount, modName, modId, String.join(", ", modules.stream().map(module ->
                ReflectionHelper.getSimpleClassName(module.getModuleClassName())).collect(Collectors.toList()))));
    }

    @SuppressWarnings("deprecation")
    public void listen(@NotNull FMLModContainer container, @NotNull FMLEvent event) {
        LOG_MODULES.trace(String.format("Listening %s event for %s (%s) mod", event.getEventType(), modName, modId));
        val bar = ProgressManager.push("Modules", modules.size(), true);
        modules.forEach(module -> {
            bar.step(ReflectionHelper.getSimpleClassName(module.getModuleClassName()));
            module.tryListen(container, event);
        });
        ProgressManager.pop(bar);
    }
}
